package edu.washington.pypark.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Spinner
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import java.text.NumberFormat
import java.util.Locale
import android.content.Context
import android.widget.AdapterView
import android.view.View
import android.widget.ArrayAdapter


class MainActivity : AppCompatActivity() {
    private val currencyFormatters : Regex = Regex("[$,.]")
    private val format = NumberFormat.getCurrencyInstance(Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<EditText>(R.id.plain_text_input)
        val button: Button = findViewById<Button>(R.id.tip_button)
        val tipList = arrayOf("10%", "15%", " 18%", "20%")
        var tip = "10%"
        var current: String = ""

        if (input.getText().toString() == "") {
            button.setEnabled(false)
        }

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                button.setEnabled(true)
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (!s.toString().equals(current)) {
                    input.removeTextChangedListener(this)
                    var cleanString = s.toString().replace(currencyFormatters, "")
                    var parsedNum = cleanString.toDouble()
                    var formatted = format.format((parsedNum / 100))
                    input.setText(formatted)
                    input.setSelection(formatted.length)
                    current = formatted
                    input.addTextChangedListener(this)
                }
            }
        })

        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipList)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    tip = getString(R.string.selected_item)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
        tipButton(input, button, tip, spinner)
    }

    fun tipButton(input: EditText, button: Button, tip: String, spinner: Spinner) {
        button.setOnClickListener {
            var tipAmount: Double = (input.text.toString().replace(currencyFormatters, "")).toDouble()
            val tipPercentage: Double = cleanTip(tip, spinner)
            tipAmount = (tipAmount / 100) * tipPercentage
            val test: String = "%.2f".format(tipAmount)
            val formatted = format.format(test.toDouble())
            toast(formatted.toString())
        }
    }

    private fun cleanTip(tip: String, spinner: Spinner): Double {
        val test = spinner.getSelectedItem().toString()
        val cleanTip: String = test.replace("%", "")
        return cleanTip.toDouble() / 100.00
    }

    private fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
