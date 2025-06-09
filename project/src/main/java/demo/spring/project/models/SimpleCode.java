package demo.spring.project.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "simple_code")
public class SimpleCode {
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
    private String local_path;
    public String getLocal_path() {
        return local_path;
    }
    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String classname;
    public String getClassname() {
        return classname;
    }
    public void setClassname(String classname) {
        this.classname = classname;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String packages_str;
    public String getPackages_str() {
        return packages_str;
    }
    public void setPackages_str(String packages_str) {
        this.packages_str = packages_str;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String addresses_str;
    public String getAddresses_str() {
        return addresses_str;
    }
    public void setAddresses_str(String addresses_str) {
        this.addresses_str = addresses_str;
    }
    @Column(columnDefinition = "TEXT", nullable = false)
    private String tablename;
    public String getTablename() {
        return tablename;
    }
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
}
