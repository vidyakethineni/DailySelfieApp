package com.example.project9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.project9.databinding.ImageItemBinding
/**
 * Fragment displaying details of a selected image.
 * This fragment is responsible for inflating the layout and displaying the details
 * of the selected image using Glide for image loading.
 */
class ImageDetailsFragment : Fragment() {
    // ViewBinding object for the layout
    private lateinit var binding:ImageItemBinding
    /**
     * Inflates the layout for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ImageItemBinding.inflate(inflater,container,false)
        return binding.root
    }
    /**
     * Called immediately after the view is created. Retrieves the selected image URL from the ViewModel
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve the shared ViewModel from the activity
        val viewModel:ImagesViewModel by activityViewModels()
        // Observe changes in the selectedImageUrl and update the image using Glide
        viewModel.selectedImageUrl.observe(viewLifecycleOwner){
            it?.apply {
                Glide.with(requireContext()).load(it).into(binding.image)
            }
        }
    }
}