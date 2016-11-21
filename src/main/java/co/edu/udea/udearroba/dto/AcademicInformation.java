package co.edu.udea.udearroba.dto;

/**
 * The Data Transfer Object for student's academic information entities.
 *
 * @author Diego Rendón
 */
public class AcademicInformation {
    
    // Variable names should be in spanish to match the Web Service response from OrgSistemasWebServiceClient.
    private String semestre;
    private String programa;
    private String inicioSemestre;
    private String finSemestre;
    private String promedioSemestre;
    private String promedioPrograma;
    private String promedioUniversidad;
    private String tercioProgramaNivel;
    private String creditosAcumulados;
    private String creditosfaltantes;
    private String numeroAprobadas;
    private String numeroPerdidas;
    private String numeroCanceladas;
    private String tipoHomologacion;
    private String nombrePrograma;

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getInicioSemestre() {
        return inicioSemestre;
    }

    public void setInicioSemestre(String inicioSemestre) {
        this.inicioSemestre = inicioSemestre;
    }

    public String getFinSemestre() {
        return finSemestre;
    }

    public void setFinSemestre(String finSemestre) {
        this.finSemestre = finSemestre;
    }

    public String getPromedioSemestre() {
        return promedioSemestre;
    }

    public void setPromedioSemestre(String promedioSemestre) {
        this.promedioSemestre = promedioSemestre;
    }

    public String getPromedioPrograma() {
        return promedioPrograma;
    }

    public void setPromedioPrograma(String promedioPrograma) {
        this.promedioPrograma = promedioPrograma;
    }

    public String getPromedioUniversidad() {
        return promedioUniversidad;
    }

    public void setPromedioUniversidad(String promedioUniversidad) {
        this.promedioUniversidad = promedioUniversidad;
    }

    public String getTercioProgramaNivel() {
        return tercioProgramaNivel;
    }

    public void setTercioProgramaNivel(String tercioProgramaNivel) {
        this.tercioProgramaNivel = tercioProgramaNivel;
    }

    public String getCreditosAcumulados() {
        return creditosAcumulados;
    }

    public void setCreditosAcumulados(String creditosAcumulados) {
        this.creditosAcumulados = creditosAcumulados;
    }

    public String getCreditosfaltantes() {
        return creditosfaltantes;
    }

    public void setCreditosfaltantes(String creditosfaltantes) {
        this.creditosfaltantes = creditosfaltantes;
    }

    public String getNumeroAprobadas() {
        return numeroAprobadas;
    }

    public void setNumeroAprobadas(String numeroAprobadas) {
        this.numeroAprobadas = numeroAprobadas;
    }

    public String getNumeroPerdidas() {
        return numeroPerdidas;
    }

    public void setNumeroPerdidas(String numeroPerdidas) {
        this.numeroPerdidas = numeroPerdidas;
    }

    public String getNumeroCanceladas() {
        return numeroCanceladas;
    }

    public void setNumeroCanceladas(String numeroCanceladas) {
        this.numeroCanceladas = numeroCanceladas;
    }

    public String getTipoHomologacion() {
        return tipoHomologacion;
    }

    public void setTipoHomologacion(String tipoHomologacion) {
        this.tipoHomologacion = tipoHomologacion;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }
    
}
