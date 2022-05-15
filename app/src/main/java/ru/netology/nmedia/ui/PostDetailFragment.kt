package ru.netology.nmedia.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import ru.netology.nmedia.databinding.PostDetailFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel

class PostDetailFragment : Fragment() {

    private var _binding: PostDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var caller: String? = null

    private val viewModel by activityViewModels<PostViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PostDetailFragmentBinding.inflate(inflater, container, false)

        val incomeArguments = getArguments()
        var textToEdit: String? = null

        var callback: OnBackPressedCallback


        if (incomeArguments != null){
            caller = incomeArguments.getString(KEY_RESULT_TYPE)
            textToEdit = incomeArguments.getString(INCOMING_KEY, "")
        } else{
            caller = REQUEST_EDIT_KEY
            textToEdit = ""
        }

        if (textToEdit!!.isBlank()) {
            callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
                val outText = binding.editText.text
                viewModel.draftContent = if (!outText.isNullOrBlank()) outText.toString() else null
                parentFragmentManager.popBackStack()
            }

            textToEdit = viewModel.draftContent ?: ""
        }


        binding.editText.setText(textToEdit)

        binding.editText.requestFocus()
        binding.okFab.setOnClickListener {
            onOkButtonClicked()
        }

        return binding.root
    }

    private fun onOkButtonClicked() {
        val outText = binding.editText.text

        if (!outText.isNullOrBlank()) {
            val resultBundle = Bundle(1) // Number of fields in a bundle
            val content = outText.toString()
            resultBundle.putString(RESULT_KEY, content)
            setFragmentResult(caller!!, resultBundle)
        }

        viewModel.draftContent = null

        parentFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        const val KEY_RESULT_TYPE = "whichResult"
        const val RESULT_KEY = "postNewContent"
        const val REQUEST_EDIT_KEY = "requestNewContent"
        const val REQUEST_CARDEDIT_KEY = "requestCardEditContent"
        const val INCOMING_KEY = "postInitialContent"
    }

}