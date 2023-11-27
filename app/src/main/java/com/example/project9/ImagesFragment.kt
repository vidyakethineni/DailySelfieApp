package com.example.project9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.project9.databinding.FragmentImagesBinding
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.util.Objects
import kotlin.math.sqrt

/**
 * Fragment for displaying a list of images and handling shake events to navigate to the camera fragment.
 * This fragment uses a RecyclerView to display images and a shake gesture to navigate to the camera fragment.
 */
class ImagesFragment : Fragment() {
    // Tag for logging purposes
    val TAG = "ImagesFragment"
    // Sensor related variables
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    // ViewBinding object for the layout
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!
    /**
     * Inflates the layout for the fragment and sets up data binding with the ViewModel.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : ImagesViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return view
    }
    /**
     * Called immediately after the view is created. Sets up the RecyclerView and observes changes in the image list.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel : ImagesViewModel by activityViewModels()
        // Set up the RecyclerView and observe changes in the image list
        val adapter = ImageAdapter(this.requireContext()){
            viewModel.setSelectedImage(it)
            findNavController().navigate(R.id.action_ImagesFragment_to_imageDetailsFragment)
        }
        binding.recyclerViewImages.adapter =adapter
        viewModel.images.observe(viewLifecycleOwner){
            imageList->
             adapter.submitList(imageList)
        }


        // Initialize and register the accelerometer sensor for shake detection
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)!!
            .registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH


    }
    /**
     * Sensor event listener for detecting shake gestures. Navigates to the camera fragment upon detecting a shake.
     */
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Display a Toast message if
            // acceleration value is over 12
            if (acceleration > 12) {
                findNavController().navigate(R.id.action_ImagesFragment_to_cameraFragment)
                Toast.makeText(requireContext(), "Shake event detected", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }
    /**
     * Registers the sensor listener when the fragment resumes.
     */
    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }
    /**
     * Unregisters the sensor listener when the fragment is paused.
     */
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }





}