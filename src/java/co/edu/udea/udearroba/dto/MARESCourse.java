package co.edu.udea.udearroba.dto;

/**
 * The Data Transfer Object for the courses entities.
 * 
 * @author Diego Rendón
 */
public class MARESCourse {
    
    // Variable names should be in spanish to match the Web Service response from OrgSistemasWebServiceClient.
    private String semestre;
    private String materia;
    private String codigo;
    private String nombre;
    private String grupo;
    private String creditos;
    private String tipoHomologacion;
    private String nota;

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCreditos() {
        return creditos;
    }

    public void setCreditos(String creditos) {
        this.creditos = creditos;
    }

    public String getTipoHomologacion() {
        return tipoHomologacion;
    }

    public void setTipoHomologacion(String tipoHomologacion) {
        this.tipoHomologacion = tipoHomologacion;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
    
    
}
