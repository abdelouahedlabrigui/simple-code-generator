package demo.spring.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "simple_code")
public class CodeMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String entity_name;
    public String getEntity_name() {
        return entity_name;
    }
    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String entity_path;
    public String getEntity_path() {
        return entity_path;
    }
    public void setEntity_path(String entity_path) {
        this.entity_path = entity_path;
    }

    private Integer entity_size;
    public Integer getEntity_size() {
        return entity_size;
    }
    public void setEntity_size(Integer entity_size) {
        this.entity_size = entity_size;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String repository_name;
    public String getRepository_name() {
        return repository_name;
    }
    public void setRepository_name(String repository_name) {
        this.repository_name = repository_name;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String repository_path;
    public String getRepository_path() {
        return repository_path;
    }
    public void setRepository_path(String repository_path) {
        this.repository_path = repository_path;
    }

    private Integer repository_size;
    public Integer getRepository_size() {
        return repository_size;
    }
    public void setRepository_size(Integer repository_size) {
        this.repository_size = repository_size;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String controller_name;
    public String getController_name() {
        return controller_name;
    }
    public void setController_name(String controller_name) {
        this.controller_name = controller_name;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String controller_path;
    public String getController_path() {
        return controller_path;
    }
    public void setController_path(String controller_path) {
        this.controller_path = controller_path;
    }

    private Integer controller_size;
    public Integer getController_size() {
        return controller_size;
    }
    public void setController_size(Integer controller_size) {
        this.controller_size = controller_size;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String created_at;
    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
