package com.emerscience.seguimientos.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputLayout;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utileria {

    /**
     * Validacion de cedula
     *
     * @param cedula Recibe campo cedula como String
     * @return true si la cedula es correcta
     */
    public static boolean validarCedulaVerificador(String cedula) {
        if (validarCodigoProvincia(cedula.substring(0, 2))) {
            if (validarTercerDigito(String.valueOf(cedula.charAt(2)))) {
                if (algoritmoMod10(cedula, Integer.parseInt(String.valueOf(cedula.charAt(9))))) {
                    System.out.println("Cedula valida");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Validacion codigo provincial cedula
     *
     * @param codigoProvincia String código de Provincia
     * @return true si el código de provincia está entre 01 y 24
     */
    private static boolean validarCodigoProvincia(String codigoProvincia) {
        return (Integer.parseInt(codigoProvincia) > 0) && (Integer.parseInt(codigoProvincia) <= 24);
    }

    /**
     * Validacion tercer digito cedula
     *
     * @param tercerDigito String tercer dígito cédula
     * @return true si el tecer dígito está entre 0 y 5
     */
    private static boolean validarTercerDigito(String tercerDigito) {
        return (Integer.parseInt(tercerDigito) >= 0) && (Integer.parseInt(tercerDigito) <= 5);
    }

    /**
     * Validacion digito verificador
     *
     * @param digitos String nueve primeros dígitos de cédula
     * @param digitoVerificador String último dígito cédula
     * @return true si el último dígito es válido según los nueve primeros dígitos de la cédula
     */
    private static boolean algoritmoMod10(String digitos, int digitoVerificador) {
        Integer[] coeficiente = new Integer[]{2, 1, 2, 1, 2, 1, 2, 1, 2};
        Integer[] digitosTemp = new Integer[digitos.length()];
        int indice = 0;
        for (char dig : digitos.toCharArray()) {
            digitosTemp[indice] = Integer.parseInt(Character.toString(dig));
            indice++;
        }

        int total = 0;
        int key = 0;
        for (Integer valorPosicion : digitosTemp) {
            if (key < coeficiente.length) {
                valorPosicion = (digitosTemp[key] * coeficiente[key]);
                if (valorPosicion >= 10) {
                    char[] valorPosicionSplit = String.valueOf(valorPosicion).toCharArray();
                    valorPosicion = (Integer.parseInt(String.valueOf(valorPosicionSplit[0]))) + (Integer.parseInt(String.valueOf(valorPosicionSplit[1])));
                }
                total += valorPosicion;
            }
            key++;
        }

        int residuo = total % 10;
        int resultado;

        if (residuo == 0) {
            resultado = 0;
        } else {
            resultado = 10 - residuo;
        }
        return resultado == digitoVerificador;
    }

    /**
     * Validacion campos nombre
     * @return true si el campo no es vacío
     */
    public static boolean validarNombres(EditText edtNombres) {
        String apellidos = edtNombres.getText().toString().trim();
        if (apellidos.isEmpty()) {
            edtNombres.setError("Campo requerido");
            return false;
        } else {
            edtNombres.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo cedula
     *
     * @param edtCedula Campo de vista dónde se ingresa cédula a validar
     * @return true si el campo de cédula no es vacío, tiene longitud de 10 dígitos, y el número de
     * cédula aún no se ha ingresado al sistema
     */
    public static boolean validarCedula(EditText edtCedula) {
        String cedula = edtCedula.getText().toString().trim();
        if (cedula.isEmpty()) {
            edtCedula.setError("Campo requerido");
            return false;
        } else if (cedula.length() < 10) {
            edtCedula.setError("Debe ingresar 10 digitos");
            return false;
        } else if (!validarCedulaVerificador(cedula)) {
            edtCedula.setError("Número de cédula no válido");
            return false;
        } else {
            edtCedula.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo Frecuencia Respiratoria
     * @return true si campo Frecuencia Respiratoria no es vacío, negativo ni muy alto
     */
    public static boolean validarFrecResp(TextInputLayout txtInputFrecResp) {
        String edad = Objects.requireNonNull(txtInputFrecResp.getEditText()).getText().toString().trim();
        if (edad.isEmpty()) {
            txtInputFrecResp.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(edad) < 0) {
            txtInputFrecResp.setError("Valor no puede ser negativo");
            return false;
        } else if (Integer.parseInt(edad) > 60) {
            txtInputFrecResp.setError("Valor muy alto");
            return false;
        } else {
            txtInputFrecResp.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo Temperatura
     * @return true si campo Temperatura no es vacío, muy bajo, ni muy alto
     */
    public static boolean validarTemp(TextInputLayout txtInputTemp) {
        String temp = Objects.requireNonNull(txtInputTemp.getEditText()).getText().toString().trim();
        if (temp.isEmpty()) {
            txtInputTemp.setError("Campo requerido");
            return false;
        } else if (Double.parseDouble(temp) < 32.0) {
            txtInputTemp.setError("Valor muy bajo");
            return false;
        } else if (Double.parseDouble(temp) > 41.0) {
            txtInputTemp.setError("Valor muy alto");
            return false;
        } else {
            txtInputTemp.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo Frecuencia cardiaca
     * @return true si campo Frecuencia cardiaca no es vacío, muy bajo, ni muy alto
     */
    public static boolean validarFrecCard(TextInputLayout txtInputFrecCard) {
        String temp = Objects.requireNonNull(txtInputFrecCard.getEditText()).getText().toString().trim();
        if (temp.isEmpty()) {
            txtInputFrecCard.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(temp) < 20) {
            txtInputFrecCard.setError("Valor muy bajo");
            return false;
        } else if (Integer.parseInt(temp) > 150) {
            txtInputFrecCard.setError("Valor muy alto");
            return false;
        } else {
            txtInputFrecCard.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo Saturación oxígeno
     * @return true si campo Saturación oxígeno no es vacío, muy bajo, ni muy alto
     */
    public static boolean validarSatOxg(TextInputLayout txtInputSatOxg) {
        String temp = Objects.requireNonNull(txtInputSatOxg.getEditText()).getText().toString().trim();
        if (temp.isEmpty()) {
            txtInputSatOxg.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(temp) < 30) {
            txtInputSatOxg.setError("Valor muy bajo");
            return false;
        } else if (Integer.parseInt(temp) > 100) {
            txtInputSatOxg.setError("Valor muy alto");
            return false;
        } else {
            txtInputSatOxg.setError(null);
            return true;
        }
    }

    /**
     * Validador de Selección RadioButton
     *
     * @param rbg Radio button group que contiene las opciones Si y No
     * @param rb Radio Button para mostrar el error en caso que no se seleecione ninguna opción
     * @return true si selección no es vacía
     */
    @SuppressLint("ResourceType")
    public static boolean validarSeleccion(RadioGroup rbg, RadioButton rb) {
        if (rbg.getCheckedRadioButtonId() <= 0) {
            rb.setError("Seleccione una opción");
            return false;
        } else {
            rb.setError(null);
            return true;
        }
    }

    public static boolean validarUsuario(TextInputLayout txtInputUsuario){
        String temp = Objects.requireNonNull(txtInputUsuario.getEditText()).getText().toString().trim();
        if (temp.isEmpty()){
            txtInputUsuario.setError("Campo Usuario es requerido");
            return false;
        }else if (temp.length() < 4){
            txtInputUsuario.setError("Ingrese un usuario de por lo menos 4 caractéres");
            return false;
        }else {
            txtInputUsuario.setError(null);
            return true;
        }
    }

    public static boolean validarContraseña(TextInputLayout txtInputContraseña, boolean editar){
        Pattern pat = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
        Matcher matcher = pat.matcher(Objects.requireNonNull(txtInputContraseña.getEditText()).getText().toString().trim());
        if (matcher.matches()){
            txtInputContraseña.setError(null);
            return true;
        }else{
            if (editar){
                if (Objects.requireNonNull(txtInputContraseña.getEditText()).getText().toString().trim().length() == 0){
                    txtInputContraseña.setError(null);
                    return true;
                }else{
                    txtInputContraseña.setError("Contraseña debe tener Mayúsculas, minúsculas, números, sin espacios en blanco" +
                            " y mínimo 8 caractéres");
                    return false;
                }
            }else{
                txtInputContraseña.setError("Contraseña debe tener Mayúsculas, minúsculas, números, sin espacios en blanco" +
                        " y mínimo 8 caractéres");
                return false;
            }
        }
    }

    /**
     * Permite eliminar tildes y diacriticos en una cadena de texto
     * @param texto Texto con tildes y diacriticos
     * @return texto sin tildes ni diacriticos
     */
    public static String cleanString(String texto) {
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return texto;
    }

    /**
     * Obtiene el token enviado por api para poder interactuar con los servicios de la api
     * @param context Contexto de la aplicación donde se necesite obtener el token
     * @return String token
     */
    public static String obtenerToken(Context context){
        SharedPreferences preferencesToken = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        return preferencesToken.getString("token", "");
    }

    /**
     * Permite quitar guiones y comas de una cadena de texto
     * @param cadena String que contiene comas y guiones
     * @return List<String>
     */
    public static List<String> quitarComaGuion(String cadena){
        System.out.println("CADENA EN UTILERIA" + cadena);
        List<String> respuesta = new ArrayList<>();
        if (cadena.contains("-")){
            System.out.println("CONTIENE COMA");
            String[] resultado = cadena.split("(?= - )");
            for (String aux: resultado) {
                respuesta.add(aux.replaceAll(" - ", ""));
            }
        }else if (cadena.contains(",")){
            System.out.println("CONTIENE GUION");
            String[] resultado = cadena.split("(?=, )");
            for (String aux : resultado) {
                respuesta.add(aux.replaceAll(", ", ""));
            }
        }
        return respuesta;
    }



}
