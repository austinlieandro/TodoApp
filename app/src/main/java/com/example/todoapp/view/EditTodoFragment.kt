package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentEditTodoBinding
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.DetailTodoViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditTodoFragment : Fragment(),TodoSaveChangesClick,  RadioClick {
    private lateinit var viewModel: DetailTodoViewModel
    private lateinit var dataBinding: FragmentEditTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate<FragmentEditTodoBinding>(inflater, R.layout.fragment_edit_todo, container, false)
        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailTodoViewModel::class.java)
        dataBinding.radioListener = this
        dataBinding.radioListener = this
        dataBinding.saveListener = this

        var txtJudulTodo = view.findViewById<TextView>(R.id.txtJudulTodo)
        var btnAdd = view.findViewById<Button>(R.id.btnAdd)

        txtJudulTodo.setText("Edit Todo")
        btnAdd.setText("Save Changes")

//        btnAdd.setOnClickListener{
//            val radio = view.findViewById<RadioGroup>(R.id.radioGroupPriority).checkedRadioButtonId
//            val txtTitle = view.findViewById<TextInputEditText>(R.id.txtTitle)
//            var txtNotes = view?.findViewById<TextInputEditText>(R.id.txtNotes)
//            viewModel.update(txtTitle.text.toString(), txtNotes?.text.toString(), radio.toString().toInt(), uuid = id)
//            Toast.makeText(view.context, "Todo Update", Toast.LENGTH_SHORT).show()
//            Navigation.findNavController(it).popBackStack()
//        }

        val uuid = EditTodoFragmentArgs.fromBundle(requireArguments()).uuid
        viewModel.fetch(uuid)
        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            dataBinding.todo = it
            var txtTitle = view?.findViewById<TextInputEditText>(R.id.txtTitle)
            var txtNotes = view?.findViewById<TextInputEditText>(R.id.txtNotes)

            txtTitle?.setText(it.title)
            txtNotes?.setText(it.notes)

            var radioLow = view?.findViewById<RadioButton>(R.id.radioLow)
            var radioMedium = view?.findViewById<RadioButton>(R.id.radioMedium)
            var radioHigh = view?.findViewById<RadioButton>(R.id.radioHigh)
            when (it.priority) {
                1 -> radioLow?.isChecked = true
                2 -> radioMedium?.isChecked = true
                else -> radioHigh?.isChecked = true
            }
        })
    }

    override fun onRadioClick(v: View, priority: Int, obj: Todo) {
        obj.priority = priority
    }

    override fun onTodoSaveChangesClick(v: View, obj: Todo) {
        viewModel.update(obj.title, obj.notes, obj.priority, obj.uuid)
        Toast.makeText(v.context, "Todo Updated", Toast.LENGTH_SHORT).show()

    }
}