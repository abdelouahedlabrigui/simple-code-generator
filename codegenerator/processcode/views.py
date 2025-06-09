import json
import os
from django.http import JsonResponse
from django.shortcuts import render
from rest_framework.decorators import api_view
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings # Import settings to get BASE_DIR
from rest_framework.response import Response
from rest_framework import status
import requests
import pandas as pd
from datetime import datetime

def to_camel_case(snake_str):
    """Converts a space-separated string to camelCase."""
    components = snake_str.lower().split(' ') # Convert to lowercase, then split by spaces
    # Capitalize the first letter of each component except the first one
    # and join them to get camelCase.
    return components[0] + ''.join(x.title() for x in components[1:])

# Create your views here.

@api_view(["POST"]) # Change to POST since you're posting data
@csrf_exempt
def post_dataset(request):
    # Ensure this is a POST request, as we are sending data.
    # The @api_view(["POST"]) decorator handles this for us, but it's good to be explicit.
    if request.method != "POST":
        return Response(
            {'Message': 'This endpoint only accepts POST requests.'},
            status=status.HTTP_405_METHOD_NOT_ALLOWED
        )

    try:
        # Read data from POST body (e.g., JSON)
        csv_path = request.data.get("csv_path")
        request_url = request.data.get("request_url")

        if not csv_path or not csv_path.endswith(".csv"):
            return Response({'Message': 'Invalid csv_path. Must be a .csv file.'}, status=400)
        if not request_url.startswith("http"):
            return Response({'Message': 'Invalid request_url. Must be a valid HTTP/HTTPS URL.'}, status=400)

        # Read the CSV file into a pandas DataFrame
        df: pd.DataFrame = pd.read_csv(csv_path, encoding='utf-8', on_bad_lines='skip').fillna(0)
        print(df)
        column_mapping = {col: to_camel_case(col) for col in df.columns}
        df = df.rename(columns=column_mapping)
        # List to store results of each POST request
        post_results = []
        errors = []

        # Iterate through each row of the DataFrame
        for index, row in df.iterrows():
            # Convert the row to a dictionary.
            # You might need to adjust this based on the expected format of your request_url API.
            # For example, if the API expects a specific nested structure, you'd build that here.
            data_to_post = row.to_dict()
            try:
                # Send a POST request for each row
                # headers can be adjusted based on what the target API expects, e.g., 'Content-Type': 'application/json'
                headers = {'Content-Type': 'application/json'}
                
                response = requests.post(request_url, json=data_to_post, headers=headers)
                response.raise_for_status() # Raise an HTTPError for bad responses (4xx or 5xx)

                post_results.append({
                    'row_index': index,
                    'status_code': response.status_code,
                    'response_data': response.json() if response.content else None, # Attempt to parse JSON if content exists
                    'success': True
                })

            except requests.exceptions.RequestException as e:
                # Catch any errors during the HTTP request (e.g., network issues, invalid URL)
                errors.append({
                    'row_index': index,
                    'error': f'Request failed for row {index}: {str(e)}',
                    'success': False
                })
            except json.JSONDecodeError:
                # Handle cases where the response is not valid JSON
                errors.append({
                    'row_index': index,
                    'error': f'Failed to decode JSON response for row {index}. Raw response: {response.text}',
                    'success': False
                })
            except Exception as e:
                errors.append({
                    'row_index': index,
                    'error': f'An unexpected error occurred during POST for row {index}: {str(e)}',
                    'success': False
                })

        # Summarize the results
        if errors:
            return Response(
                {
                    'Message': 'CSV data posting completed with some errors.',
                    'successful_posts': len(post_results),
                    'failed_posts': len(errors),
                    'results': post_results,
                    'errors': errors
                },
                status=status.HTTP_207_MULTI_STATUS # Partial content / Multi-Status
            )
        else:
            return Response(
                {
                    'Message': 'All CSV data posted successfully.',
                    'successful_posts': len(post_results),
                    'results': post_results
                },
                status=status.HTTP_200_OK
            )

    except FileNotFoundError:
        return Response(
            {'Message': f'CSV file not found at {csv_path}'},
            status=status.HTTP_404_NOT_FOUND
        )
    except pd.errors.EmptyDataError:
        return Response(
            {'Message': f'CSV file at {csv_path} is empty.'},
            status=status.HTTP_400_BAD_REQUEST
        )
    except Exception as e:
        # Catch any other unexpected errors during file reading or initial processing
        return Response(
            {'Message': f'An unexpected error occurred: {str(e)}'},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR
        )


# Helper function to convert column names to camelCase for Java variable names
def to_camel_case(snake_str: str) -> str:
    """
    Converts a string (e.g., 'Product ID', 'product_name') to camelCase.
    """
    if not snake_str:
        return ""
    # Replace non-alphanumeric with spaces, then split, then join
    components = snake_str.replace('_', ' ').replace('-', ' ').split(' ')
    # Filter out empty strings from split
    components = [c for c in components if c]
    if not components:
        return ""
    
    # Capitalize the first letter of each component except the first one
    # and join them to get camelCase.
    return components[0].lower() + ''.join(x.capitalize() for x in components[1:])


# Helper function to generate Java property strings (more robust)
def generate_java_property_line(java_type: str, java_variable_name: str, column_name: str = None) -> str:
    """
    Generates a Java property string, optionally with a @Column annotation.
    """
    column_name = column_name.replace('-', '_')
    column_annotation = ""
    # Only add @Column if column_name is provided and is different from the java_variable_name
    # (assuming the database column name matches the Java variable name by default if not specified)
    if java_type == "object":
        column_annotation = f'    @Column(name = "prefix_{column_name}", columnDefinition = "TEXT", nullable = false)\n'
    else:
        column_annotation = f'    @Column(name = "prefix_{column_name}")\n'
    return f'{column_annotation}    private {java_type} {java_variable_name};'


def post_dataset_form_view(request):
    """
    Renders the HTML form to submit data to the post_dataset API endpoint.
    """
    return render(request, 'gen/post_dataset_form.html')


def dataset_form_view(request):
    return render(request, 'gen/dataset_form.html')

@api_view(["POST"]) # Changed to POST because you're creating/modifying a file
@csrf_exempt
def dataset_information(request):
    if request.method != "POST":
        return Response(
            {'Message': 'This endpoint only accepts POST requests.'},
            status=status.HTTP_405_METHOD_NOT_ALLOWED
        )

    local_path: str = request.data.get('local_path')
    classname: str = request.data.get('classname')
    packages_str: str = request.data.get('packages_str')
    addresses_str: str = request.data.get('addresses_str')
    tablename: str = request.data.get('tablename')
    print("local_path", local_path)
    print("classname", classname)
    print("packages_str", packages_str)
    print("addresses_str", addresses_str)
    print("tablename", tablename)
    # Basic input validation for security and correctness
    if not classname.strip() or not tablename.strip():
        return Response(
            {'Message': 'Classname and tablename cannot be empty.'},
            status=status.HTTP_400_BAD_REQUEST
        )

    # Process comma-separated strings
    packages = [p.strip() for p in packages_str.split(',') if p.strip()]
    addresses = [a.strip() for a in addresses_str.split(',') if a.strip()]

    if not packages:
        return Response(
            {'Message': 'At least one package name is required.'},
            status=status.HTTP_400_BAD_REQUEST
        )
    if not addresses:
        return Response(
            {'Message': 'At least one address (output directory) is required.'},
            status=status.HTTP_400_BAD_REQUEST
        )


    try:
        if not local_path.lower().endswith(".csv"):
            return Response({"Message": "Invalid file type. Only .csv files are supported."}, 
                            status=status.HTTP_400_BAD_REQUEST)

        if not os.path.exists(local_path):
            return Response(
                {'Message': f'CSV file not found at {local_path}'},
                status=status.HTTP_404_NOT_FOUND
            )

        df: pd.DataFrame = pd.read_csv(local_path, encoding='utf-8', on_bad_lines='skip')

        # Mapping Pandas dtypes to common Java types for Spring Boot
        # Expanded mappings for better accuracy
        dtype_to_java_map = {
            'int64': 'Long',
            'float64': 'Double',
            'object': 'String',
            'bool': 'Boolean',
            'datetime64[ns]': 'java.time.LocalDateTime', # Or LocalDate, ZonedDateTime, Timestamp
            'int32': 'Integer',
            'float32': 'Float',
            # Add more specific mappings if needed (e.g., for specific string patterns to Enum)
        }

        columns_info = []
        generated_properties_lines = [] # Renamed for clarity

        for column in df.columns:
            col_dtype = str(df[column].dtypes)
            
            # Append column information
            columns_info.append({
                "filename": os.path.basename(local_path), # Better name than basename
                "column_name": column, # More descriptive than column_str
                "dtype": col_dtype,
                "count_rows": int(df[column].count()),
                "count_nan": int(df[column].isna().sum())
            })

            # Generate Spring Boot property
            java_type = dtype_to_java_map.get(col_dtype, 'String') # Default to String if type not mapped

            # Convert original column name to camelCase for Java variable name
            java_variable_name = to_camel_case(column)

            # Generate the property line, potentially with @Column annotation
            # We pass the original column name as column_name for @Column annotation
            # if the database column name is different from the Java variable name.
            property_line = generate_java_property_line(java_type, java_variable_name, column)
            generated_properties_lines.append(property_line)

        # --- File Generation Logic ---
        # Construct path to the template file
        # Assume 'entity.txt' is in a 'templates' folder within your Django app's 'jobs' directory
        template_dir = os.path.join(settings.BASE_DIR, 'processcode', 'templates')
        entity_template_path = os.path.join(template_dir, 'entity.txt')
        repository_template_path = os.path.join(template_dir, "repository.txt")
        controller_template_path = os.path.join(template_dir, "controller.txt")


        # Construct path for the output Java file
        # SECURITY NOTE: Restrict 'addresses[0]' to a safe, controlled output directory.
        # Do NOT allow arbitrary paths to prevent writing to sensitive system locations.
        # Example: Ensure the target directory is a subdirectory of a known safe output root.
        target_base_dir = os.path.join(settings.BASE_DIR, 'generated_java_entities') # Define a safe root
        os.makedirs(target_base_dir, exist_ok=True) # Ensure this root exists

        # Further restrict the specific path if addresses[0] contains arbitrary subfolders
        # For simplicity, assuming addresses[0] might be empty or a simple subfolder name.
        # If addresses[0] is meant to be a full path from the user, it's a security risk.
        entity_output_dir = os.path.join(target_base_dir, addresses[0]) # Use addresses[0] as a subfolder
        os.makedirs(entity_output_dir, exist_ok=True) # Create the specific output folder

        entity_java_path = os.path.join(entity_output_dir, f'{classname}.java')


        with open(entity_template_path, 'r', encoding='utf-8') as f_template:
            template_content = f_template.read() # Read the template content once

        # Perform replacements on the template content
        template_content = template_content.replace('[package]', packages[0]) # packages[0]: 0 is entity
        template_content = template_content.replace('[tablename]', tablename)
        template_content = template_content.replace('[classname]', classname)

        # Join all generated properties into a single string for insertion
        properties_block = "\n".join(generated_properties_lines)
        template_content = template_content.replace('[properties]', properties_block + "\n")

        # Write the final processed content to the new Java file once
        with open(entity_java_path, "w", encoding='utf-8') as f_java:
            f_java.write(template_content)

        
        repository_entity = f"{str(packages[0]).replace('package ', 'import ').replace(';', '')}.{classname};" # packages[1]: is repository
        with open(repository_template_path, 'r', encoding='utf-8') as f_template:
            repository_template_content = f_template.read() # Read the template content once

        repository_template_content = repository_template_content.replace('[package]', packages[1])
        repository_template_content = repository_template_content.replace('[classname]', classname)
        repository_template_content = repository_template_content.replace('[entitynamespace]', repository_entity)
        repository_address = addresses[1]
        repository_java_path = os.path.join(repository_address, f'{classname}Repository.java')


        # Write the final processed content to the new Java file once
        with open(repository_java_path, "w", encoding='utf-8') as f_java:
            f_java.write(repository_template_content)

        controller_package = packages[2]
        entity_import = f"{str(packages[0]).replace('package ', 'import ').replace(';', '')}.{classname};"
        repository_import = f"{str(packages[1]).replace('package ', 'import ').replace(';', '')}.{classname}Repository;"
        with open(controller_template_path, 'r', encoding='utf-8') as f_template:
            controller_template_content = f_template.read() # Read the template content once
        controller_template_content = controller_template_content.replace('[package]', controller_package)
        controller_template_content = controller_template_content.replace('[entitynamespace]', entity_import)
        controller_template_content = controller_template_content.replace('[classname]', classname)
        controller_template_content = controller_template_content.replace('[repositorynamespace]', repository_import)
        controller_template_content = controller_template_content.replace('[apiendpoint]', tablename)

        controller_address = addresses[2]
        controller_java_path = os.path.join(controller_address, f'{classname}Controller.java')
        # Write the final processed content to the new Java file once
        with open(controller_java_path, "w", encoding='utf-8') as f_java:
            f_java.write(controller_template_content)

        data = {
            "entity_name": f"{classname}.java",
            "entity_path": entity_java_path,
            "entity_size": int(os.path.getsize(entity_java_path)),
            "repository_name": f"{classname}Repository.java",
            "repository_path": repository_java_path,
            "repository_size": int(os.path.getsize(repository_java_path)),
            "controller_name": f"{classname}Controller.java",
            "controller_path": controller_java_path,
            "controller_size": int(os.path.getsize(controller_java_path)),
            "created_at": str(datetime.now())
        }
        headers = {"Accept": "application/json", "Content-Type": "application/json"}
        requests.post("http://localhost:8081/api/codes/post-code-metadata", json=data, headers=headers)

        requests.post("http://localhost:8081/api/codes/post-simple-code", json={
            "local_path": local_path,
            "classname": classname,
            "packages_str": packages_str,
            "addresses_str": addresses_str,
            "tablename": tablename,
        }, headers=headers)

        return Response(data, status=status.HTTP_200_OK)

    except pd.errors.EmptyDataError:
        return Response(
            {'Message': f'CSV file at {local_path} is empty or has no data.'},
            status=status.HTTP_400_BAD_REQUEST
        )
    except FileNotFoundError:
        return Response(
            {'Message': f'Template file not found at {entity_template_path}. Please ensure it exists.'},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR # Server-side issue if template is missing
        )
    except IndexError:
        return Response(
            {'Message': 'Error processing packages or addresses. Ensure they are comma-separated and not empty.'},
            status=status.HTTP_400_BAD_REQUEST
        )
    except Exception as e:
        # Catch any other unexpected errors
        return Response(
            {'Message': f'An unexpected error occurred: {str(e)}'},
            status=status.HTTP_500_INTERNAL_SERVER_ERROR
        )