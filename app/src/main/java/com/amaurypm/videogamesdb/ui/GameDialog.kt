package com.amaurypm.videogamesdb.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.amaurypm.videogamesdb.R
import com.amaurypm.videogamesdb.application.VideogamesDBApp
import com.amaurypm.videogamesdb.data.GameRepository
import com.amaurypm.videogamesdb.data.db.model.GameEntity
import com.amaurypm.videogamesdb.databinding.GameDialogBinding
import com.amaurypm.videogamesdb.util.DevsList
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Creado por Amaury Perea Matsumura el 01/09/23
 */
class GameDialog(

    private val newGame: Boolean = true,
    private var game: GameEntity = GameEntity(
        title = "",
        genre = "",
        developer = "",
        devImage = 0
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
) : DialogFragment() {

    private var _binding: GameDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: GameRepository

    var flag_Spinner : Boolean = false

    var dev_Image_Actual = 6

    //Se configura el diálogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = GameDialogBinding.inflate(requireActivity().layoutInflater)

        repository = (requireContext().applicationContext as VideogamesDBApp).repository

        builder = AlertDialog.Builder(requireContext())

        /*binding.tietTitle.setText(game.title)
        binding.tietGenre.setText(game.genre)
        binding.tietDeveloper.setText(game.developer)*/

        val devs = arrayListOf(getString(R.string.activision), getString(R.string.sony), getString(R.string.nintendo),
            getString(R.string.microsoft), getString(R.string.tencent))

        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, devs)

        binding.spinner.adapter = adaptador

        binding.apply {
            tietTitle.setText(game.title)
            tietGenre.setText(game.genre)
            //tietDeveloper.setText(game.developer)
            spinner.setSelection(game.devImage)
            dev_Image_Actual = game.devImage
        }

        dialog = if (newGame) {
            buildDialog(getString(R.string.save), getString(R.string.cancel), {
                 //Create (Guardar)
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                //game.developer = binding.tietDeveloper.text.toString()
                game.devImage = binding.spinner.selectedItemPosition

                try {
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }

                    message(getString(R.string.juego_guardado))

                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.error_save))
                }
            }, {
                //Cancelar
            })
        } else {
            buildDialog(getString(R.string.update), getString(R.string.delete), {
                //Update
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                //game.developer = binding.tietDeveloper.text.toString()
                game.devImage = binding.spinner.selectedItemPosition

                try {
                    lifecycleScope.launch {
                        repository.updateGame(game)
                    }

                    message(getString(R.string.successfully_updated))

                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message(getString(R.string.error_updated))
                }

            }, {
                //Delete

                val context = requireContext()

                AlertDialog.Builder(context)
                    .setTitle(getString(R.string.tittle_delete))
                    .setMessage("¿Realmente deseas eliminar el juego ${game.title}?")
                    .setPositiveButton(getString(R.string.accept)){ _,_ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteGame(game)
                            }

                            message(context.getString(R.string.successfully_delete))

                            //Actualizar la UI
                            updateUI()

                        }catch(e: IOException){
                            e.printStackTrace()
                            message(getString(R.string.error_delete))
                        }
                    }
                    .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()


            })
        }

        /*dialog = builder.setView(binding.root)
            .setTitle("Juego")
            .setPositiveButton("Guardar", DialogInterface.OnClickListener { _, _ ->
                //Guardar
                game.title = binding.tietTitle.text.toString()
                game.genre = binding.tietGenre.text.toString()
                game.developer = binding.tietDeveloper.text.toString()

                try {
                    lifecycleScope.launch {
                        repository.insertGame(game)
                    }

                    Toast.makeText(requireContext(), getString(R.string.juego_guardado), Toast.LENGTH_SHORT).show()
                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error al guardar el juego", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("Cancelar", DialogInterface.OnClickListener { _, _ ->

            })
            .create()*/

        return dialog
    }

    //Cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog =
            dialog as AlertDialog //Lo usamos para poder emplear el método getButton (no lo tiene el dialog)
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
                saveButton?.isEnabled = true
            }
        })

        binding.tietGenre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
                saveButton?.isEnabled = true
            }

        })

        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 != dev_Image_Actual){
                    flag_Spinner = true
                    saveButton?.isEnabled = validateFields()
                } else {
                    flag_Spinner = false
                    saveButton?.isEnabled = validateFields()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                flag_Spinner = false
                saveButton?.isEnabled = validateFields()
            }

        }

        /*binding.tietDeveloper.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.tietDeveloper.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(d0: CharSequence?, d1: Int, d2: Int, d3: Int) {

            }

            override fun onTextChanged(d0: CharSequence?, d1: Int, d2: Int, d3: Int) {

            }

            override fun afterTextChanged(d0: Editable?) {
                // saveButton?.isEnabled = validateFields()
                if(binding.tietDeveloper.text.toString().isNotEmpty()) {
                    binding.tilDeveloper.error = ""
                }
                val platform = when (d0.toString()) {
                    getString(R.string.activision) -> DevsList.ACTIVISION
                    getString(R.string.microsoft) -> DevsList.MICROSOFT
                    getString(R.string.nintendo) -> DevsList.NINTENDO
                    getString(R.string.sony) -> DevsList.SONY
                    getString(R.string.tencent) -> DevsList.TENCENT
                    else -> DevsList.OTHER
                }
            }

        })*/

    }

    private fun validateFields(): Boolean {
        var valid = true
        if(binding.tietTitle.text.toString().isEmpty())
        {
            //binding.tilTitle.error = getString(R.string.empty_name)
            valid = false
        }
        if(binding.tietGenre.text.toString().isEmpty())
        {
            //binding.tilGenre.error = getString(R.string.empty_genre)
            valid = false
        }
        if(!flag_Spinner) {
            valid = false
        }
        /*if(binding.tietDeveloper.text.toString().isEmpty())
        {
            binding.tilDeveloper.error = getString(R.string.empty_dev)
            valid = false
        }*/

        return valid
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle(getString(R.string.tittle_new_game))
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                //Acción para el botón positivo
                positiveButton()
            })
            .setNegativeButton(btn2Text) { _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()


}