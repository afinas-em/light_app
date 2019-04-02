package tech.bottleneck.the_lightapp;

public class Exam_model {

    private int id;
    private String tittle;
    private String category;
    private String module;
    private String status;




    public Exam_model(int id ,String tittle, String category, String module, String status) {
        this.tittle = tittle;
        this.category = category;
        this.module = module;
        this.status = status;
        this.id = id;

    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
