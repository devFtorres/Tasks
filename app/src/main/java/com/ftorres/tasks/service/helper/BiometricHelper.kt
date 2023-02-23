package com.ftorres.tasks.service.helper

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG

class BiometricHelper {

    companion object{
        fun isBiometricAvailable(context: Context): Boolean{

            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)){
                BiometricManager.BIOMETRIC_SUCCESS -> return true
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> return false
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> return false
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> return false
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> return false
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> return false
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> return false
            }

            return false
        }
    }
}