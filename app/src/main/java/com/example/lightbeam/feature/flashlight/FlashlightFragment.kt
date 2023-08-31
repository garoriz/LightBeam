package com.example.lightbeam.feature.flashlight

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.findNavController
import com.example.lightbeam.R
import com.example.lightbeam.Storage.isFlashlight
import com.example.lightbeam.databinding.FragmentFlashlightBinding

class FlashlightFragment : Fragment(R.layout.fragment_flashlight) {

    private lateinit var binding: FragmentFlashlightBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFlashlightBinding.bind(view)
        changeBackgroundColor(isFlashlight)
        changeIconColor()

        with(binding) {
            ibPower.setOnClickListener {
                if (requireContext().isFlashLightAvailable()) {
                    requireContext().toggleFlashLight(!isFlashlight)
                    isFlashlight = !isFlashlight
                    changeBackgroundColor(isFlashlight)
                }
            }

            ibColor.setOnClickListener {
                view.findNavController().navigate(
                    R.id.action_flashlightFragment_to_changeColorFragment
                )
            }
        }
    }

    private fun changeIconColor() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val defaultValue = R.color.red
        val iconColor = sharedPref.getInt(getString(R.string.icon_color), defaultValue)

        with(binding) {
            ibPower.setColorFilter(
                resources.getColor(iconColor, null),
                PorterDuff.Mode.SRC_ATOP)
            ibColor.setColorFilter(
                resources.getColor(iconColor, null),
                PorterDuff.Mode.SRC_ATOP);
        }
    }

    private fun changeBackgroundColor(isFlashlight: Boolean) {
        with(binding) {
            if (isFlashlight) {
                layout.setBackgroundResource(R.color.white)
            } else {
                layout.setBackgroundResource(R.color.black)
            }
        }
    }

    fun Context.isFlashLightAvailable() = packageManager
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

    val Context.camaraManager: CameraManager get() = getSystemService(CameraManager::class.java)


    fun Context.toggleFlashLight(on: Boolean) {
        camaraManager.run {

            val firstCameraWithFlash = cameraIdList.find { camera ->
                getCameraCharacteristics(camera).keys.any { it == FLASH_INFO_AVAILABLE }
            }

            firstCameraWithFlash?.let {
                runCatching { setTorchMode(it, on) }.onFailure {
                    Log.e(
                        it.toString(),
                        "Error setTorchMode"
                    )
                }
            } ?: Log.e(Throwable("toggleFlashLight").toString(), "Camera with flash not found")

        }
    }
}