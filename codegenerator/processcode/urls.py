from django.urls import path
from . import views
from django.views.decorators.csrf import csrf_exempt

app_name = 'processcode'

urlpatterns = [
    path('post-dataset/', views.post_dataset, name='post_dataset'),
    path('submit-post-dataset/', views.post_dataset_form_view, name='submit_post_dataset_form'),
    path(
        'dataset-info/',
        views.dataset_information,
        name='dataset_information'
    ),
    path('submit-dataset/', views.dataset_form_view, name='submit_dataset_form'),
]
