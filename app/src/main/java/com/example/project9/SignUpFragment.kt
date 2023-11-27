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
import com.example.project9.ImagesViewModel
import com.example.project9.R
import com.example.project9.databinding.FragmentSignUpBinding
/**
 * Fragment for user sign-up functionality.
 * This fragment allows users to sign up with their email and password. It observes changes
 * in the associated ViewModel for navigation events and error messages.
 */
class SignUpFragment : Fragment() {
    // Tag for logging purposes
    val TAG = "SignUpFragment"
    // ViewBinding object for the layout
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    /**
     * Inflates the layout for the fragment and sets up data binding with the ViewModel.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel : ImagesViewModel by activityViewModels()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Observe navigation events to move to the sign-in screen
        viewModel.navigateToSignIn.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_signUpFragment_to_signInFragment)
                viewModel.onNavigatedToSignIn()
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