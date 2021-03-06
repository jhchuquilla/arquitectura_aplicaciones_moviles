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
     * @param codigoProvincia
     * @return
     */
    private static boolean validarCodigoProvincia(String codigoProvincia) {
        return (Integer.parseInt(codigoProvincia) > 0) && (Integer.parseInt(codigoProvincia) <= 24);
    }

    /**
     * Validacion tercer digito cedula
     *
     * @param tercerDigito
     * @return
     */
    private static boolean validarTercerDigito(String tercerDigito) {
        return (Integer.parseInt(tercerDigito) >= 0) && (Integer.parseInt(tercerDigito) <= 5);
    }

    /**
     * Validacion digito verificador
     *
     * @param digitos
     * @param digitoVerificador
     * @return
     */
    private static boolean algoritmoMod10(String digitos, int digitoVerificador) {
        Integer coeficiente[] = new Integer[]{2, 1, 2, 1, 2, 1, 2, 1, 2};
        Integer digitosTemp[] = new Integer[digitos.length()];
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
        if (resultado != digitoVerificador) {
            return false;
        }
        return true;
    }

    /**
     * Validacion campos apellido
     *
     * @return
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
     *
     * @return
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
     * @param txtInputCedula
     * @return
     */
    public static boolean validarCedula(TextInputLayout txtInputCedula) {
        String cedula = txtInputCedula.getEditText().getText().toString().trim();
        if (cedula.isEmpty()) {
            txtInputCedula.setError("Campo requerido");
            return false;
        } else if (cedula.length() < 10) {
            txtInputCedula.setError("Debe ingresar 10 digitos");
            return false;
        } else if (!validarCedulaVerificador(cedula)) {
            txtInputCedula.setError("N??mero de c??dula no v??lido");
            return false;
        } else if (!validarCedulaRepetida(cedula)) {
            txtInputCedula.setError("El n??mero de c??dula ya se ha ingresado antes");
            return false;
        } else {
            txtInputCedula.setError(null);
            return true;
        }
    }

    /**
     * Validador de campo Edad
     *
     * @return
     */
    private static boolean validarCedulaRepetida(String cedula) {
        boolean valido = false;
        try {
            int opcion;
            if (null == DatosFamiliaresActivity.numCedulas){
                opcion = 1;
            }else {
                opcion = 2;
            }

            switch (opcion){
                case 1:
                    if(DatosPersonalesActivity.cedulasEstudiantes.contains(cedula)){
                        valido = false;
                    } else {
                        valido = true;
                    }
                    break;
                case 2:
                    if (DatosFamiliaresActivity.numCedulas.contains(cedula)) {
                        valido = false;
                    }else if(DatosPersonalesActivity.cedulasEstudiantes.contains(cedula)){
                        valido = false;
                    } else {
                        valido = true;
                    }
                    break;
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return valido;
    }

    /**
     * Validador de campo Edad
     *
     * @return
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
     * @param rbgSexo
     * @param rbFemenino
     * @return
     */
    @SuppressLint("ResourceType")
    public static boolean validarSexo(RadioGroup rbgSexo, RadioButton rbFemenino) {
        if (rbgSexo.getCheckedRadioButtonId() <= 0) {
            rbFemenino.setError("Seleccione una opci??n");
            return false;
        } else {
            rbFemenino.setError(null);
            return true;
        }
    }

    /**
     * Validador campo celular
     *
     * @param txtInputCelular
     * @return
     */
    public static boolean validarCelular(TextInputLayout txtInputCelular) {
        String celular = txtInputCelular.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("[0][9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
        if (celular.isEmpty()) {
            txtInputCelular.setError("Campo requerido");
            return false;
        } else if (celular.length() < 10) {
            txtInputCelular.setError("Debe ingresar 10 d??gitos");
            return false;
        } else if (p.matcher(celular).matches() == false) {
            txtInputCelular.setError("N??mero no v??lido, debe empezar por 09");
            return false;
        } else
            txtInputCelular.setError(null);
        return true;
    }

    /**
     * Validador campo Telefono fijo
     *
     * @return
     */
    public static boolean validarFijo(TextInputLayout txtInputFijo) {
        String fijo = txtInputFijo.getEditText().getText().toString().trim();
        if (fijo.isEmpty()){
            txtInputFijo.setError(null);
            return true;
        }else if (fijo.length() < 9){
            txtInputFijo.setError("Debe ingresar 9 d??gitos o dejar el campo vac??o");
            return true;
        }else {
            txtInputFijo.setError(null);
            return true;
        }
    }

    /**
     * Validador campo Correo electronico
     *
     * @return
     */
    public static boolean validarCorreo(TextInputLayout txtInputCorreo) {
        String correo = txtInputCorreo.getEditText().getText().toString().trim();
        if (correo.isEmpty()) {
            txtInputCorreo.setError("Campo requerido");
            return false;
        } else if (Patterns.EMAIL_ADDRESS.matcher(correo).matches() == false) {
            txtInputCorreo.setError("Correo no v??lido");
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

    public static String obtenerToken(Context context){
        SharedPreferences preferencesToken = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        return preferencesToken.getString("token", "");
    }

}
