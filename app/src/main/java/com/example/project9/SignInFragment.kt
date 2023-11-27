package com.example.project9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.project9.databinding.FragmentSignInBinding

/**
 * Fragment for user sign-in functionality.
 * This fragment allows users to sign in with their email and password. It observes changes
 * in the associated ViewModel for navigation events and error messages.
 */
class SignInFragment : Fragment() {
    // Tag for logging purposes
    val TAG = "SignInFragment"
    // ViewBinding object for the layout
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the layout for the fragment and sets up data binding with the ViewModel.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : ImagesViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Observe navigation events to move to the image list or sign-up screen
        viewModel.navigateToList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_imagesFragment)
                viewModel.onNavigatedToList()
            }
        })
        viewModel.navigateToSignUp.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signInFragment_to_signUpFragment)
                viewModel.onNavigatedToSignUp()
            }
        })
        // Observe error messages and display a Toast if an error occurs
        viewModel.errorHappened.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        })
        return view
    }


}