package com.emerscience.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.emerscience.activity.DatosFamiliaresActivity;
import com.emerscience.activity.DatosPersonalesActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
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
     * Validacion campos apellido
     * @return true si el campo no es vacío
     */
    public static boolean validarApellidos(TextInputLayout txtInputApellidos) {
        String apellidos = txtInputApellidos.getEditText().getText().toString().trim();
        if (apellidos.isEmpty()) {
            txtInputApellidos.setError("Campo requerido");
            return false;
        } else {
            txtInputApellidos.setError(null);
            return true;
        }
    }

    /**
     * Validar campos nombres
     * @return true si el campo no es vacío
     */
    public static boolean validarNombres(TextInputLayout txtInputNombres) {
        String nombres = txtInputNombres.getEditText().getText().toString().trim();
        if (nombres.isEmpty()) {
            txtInputNombres.setError("Campo requerido");
            return false;
        } else {
            txtInputNombres.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo cedula
     *
     * @param txtInputCedula Campo de vista dónde se ingresa cédula a validar
     * @return true si el campo de cédula no es vacío, tiene longitud de 10 dígitos, y el número de
     * cédula aún no se ha ingresado al sistema
     */
    public static boolean validarCedula(TextInputLayout txtInputCedula, boolean editar, List<String> cedulas) {
        String cedula = txtInputCedula.getEditText().getText().toString().trim();
        if (cedula.isEmpty()) {
            txtInputCedula.setError("Campo requerido");
            return false;
        } else if (cedula.length() < 10) {
            txtInputCedula.setError("Debe ingresar 10 digitos");
            return false;
        } else if (!validarCedulaVerificador(cedula)) {
            txtInputCedula.setError("Número de cédula no válido");
            return false;
        } else if (!validarCedulaRepetida(cedula, editar, cedulas)) {
            txtInputCedula.setError("El número de cédula ya se ha ingresado antes");
            return false;
        } else {
            txtInputCedula.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo Edad
     * @return true si el número de cédula no existe en el sistema
     */
    private static boolean validarCedulaRepetida(String cedula, boolean editar, List<String> cedulas) {
        boolean valido = false;
        try {
            if (!editar){

                if (cedulas.contains(cedula)){
                    valido = false;
                }else {
                    valido = true;
                }

//                int opcion;
//                if (null == DatosFamiliaresActivity.numCedulas){
//                    opcion = 1;
//                }else {
//                    opcion = 2;
//                }
//
//                switch (opcion){
//                    case 1:
//                        valido = !DatosPersonalesActivity.cedulasEstudiantes.contains(cedula);
//                        break;
//                    case 2:
//                        if (DatosFamiliaresActivity.numCedulas.contains(cedula)) {
//                            valido = false;
//                        }else if(DatosPersonalesActivity.cedulasEstudiantes.contains(cedula)){
//                            valido = false;
//                        } else {
//                            valido = true;
//                        }
//                        break;
//                }
            }else {
                valido = true;
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return valido;
    }

    /**
     * Validador de campo Edad
     * @return true si campo Edad no es vacío, negativo ni mayor a 120
     */
    public static boolean validarEdad(TextInputLayout txtInputEdad) {
        String edad = txtInputEdad.getEditText().getText().toString().trim();
        if (edad.isEmpty()) {
            txtInputEdad.setError("Campo requerido");
            return false;
        } else if (Integer.parseInt(edad) < 0) {
            txtInputEdad.setError("Edad no puede ser negativa");
            return false;
        } else if (Integer.parseInt(edad) > 120) {
            txtInputEdad.setError("Edad no puede ser mayor a 120");
            return false;
        } else {
            txtInputEdad.setError(null);
            return true;
        }
    }

    /**
     * Validador de Seleccion Sexo
     *
     * @param rbgSexo Radio button group que contiene las opciones Masculino y Femenino
     * @param rbFemenino Radio Button para mostrar el error en caso que no se seleecione ninguna opción
     * @return true si selección de sexo no es vacía
     */
    @SuppressLint("ResourceType")
    public static boolean validarSexo(RadioGroup rbgSexo, RadioButton rbFemenino) {
        if (rbgSexo.getCheckedRadioButtonId() <= 0) {
            rbFemenino.setError("Seleccione una opción");
            return false;
        } else {
            rbFemenino.setError(null);
            return true;
        }
    }

    /**
     * Validador campo celular
     * @param txtInputCelular Campo de ingreso de celular para indicar error en el caso que no cumpla
     *                        las validaciones
     * @return true si el campo celular no es vacío, tiene longitud de 10 dígitos y número celular
     * comienza con 09
     */
    public static boolean validarCelular(TextInputLayout txtInputCelular) {
        String celular = txtInputCelular.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("[0][9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
        if (celular.isEmpty()) {
            txtInputCelular.setError("Campo requerido");
            return false;
        } else if (celular.length() < 10) {
            txtInputCelular.setError("Debe ingresar 10 dígitos");
            return false;
        } else if (p.matcher(celular).matches() == false) {
            txtInputCelular.setError("Número no válido, debe empezar por 09");
            return false;
        } else
            txtInputCelular.setError(null);
        return true;
    }

    /**
     * Validador campo Telefono fijo
     * @return true si el telefono fijo es vacío, tiene longitud de 9 dígitos
     */
    public static boolean validarFijo(TextInputLayout txtInputFijo) {
        String fijo = txtInputFijo.getEditText().getText().toString().trim();
        if (fijo.isEmpty()){
            txtInputFijo.setError(null);
            return true;
        }else if (fijo.length() < 9){
            txtInputFijo.setError("Debe ingresar 9 dígitos o dejar el campo vacío");
            return true;
        }else {
            txtInputFijo.setError(null);
            return true;
        }
    }

    /**
     * Validador campo Correo electronico
     * @return true si el campo correo no es vacío o cumple con el patron de correo electrónico
     */
    public static boolean validarCorreo(TextInputLayout txtInputCorreo) {
        String correo = txtInputCorreo.getEditText().getText().toString().trim();
        if (correo.isEmpty()) {
            txtInputCorreo.setError("Campo requerido");
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(correo).matches() == false) {
            txtInputCorreo.setError("Correo no válido");
            return false;
        } else {
            txtInputCorreo.setError(null);
            return true;
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
