package com.example.tipcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etSum: EditText
    private lateinit var etCount: EditText
    private lateinit var etResult: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var rgDiscount: RadioGroup
    private lateinit var rg3: RadioButton
    private lateinit var rg5: RadioButton
    private lateinit var rg7: RadioButton
    private lateinit var rg10: RadioButton
    private lateinit var btnTotal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etSum = findViewById(R.id.etSum)
        etCount = findViewById(R.id.etCount)
        etResult = findViewById(R.id.etResult)
        seekBar = findViewById(R.id.seekBar)
        rgDiscount = findViewById(R.id.rgDiscount)
        rg3 = findViewById(R.id.rg3)
        rg5 = findViewById(R.id.rg5)
        rg7 = findViewById(R.id.rg7)
        rg10 = findViewById(R.id.rg10)
        btnTotal = findViewById(R.id.btnTotal)

        //SeekBar с шагом 5 и Snackbar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fronUser: Boolean) {
                val stepped = (progress / 5)
                if(progress != stepped){
                    sb?.progress = stepped
                    return
                }

                val tip = calculateTip()
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Сумма чаевых: %.2f".format(tip),
                    Snackbar.LENGTH_SHORT
                ).show()

                updateDiscount()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {            }
        })

//        Автопересчёт скидки при вводе
        val watcher = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) { updateDiscount() }
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
        }
        etCount.addTextChangedListener(watcher)
        etSum.addTextChangedListener(watcher)

    }

    //Сумма чаевых = сумма заказа * процент / 100
    private fun calculateTip(): Double{
        val sum = etSum.text.toString().toDoubleOrNull() ?: 0.0
        return sum * seekBar.progress / 100.0
    }

//    Процент скидки в зависимости от количества блюд
    private fun getDiscountPercent(): Int{
        val count = etCount.text.toString().toIntOrNull() ?: 0
        return when{
            count in 1..2 -> 3
            count in 3..5 -> 5
            count in 6..10 -> 7
            count > 10 -> 10
            else -> 0
        }
    }

//    Сумма скидки = сумма заказа * процент скидки / 100
    private  fun calculateDiscount(): Double{
        val sum = etSum.text.toString().toDoubleOrNull() ?: 0.0
        return sum * getDiscountPercent() / 100.0
    }

//    Программный выбор радиокнопки + вывод суммы скидки
    private fun updateDiscount() {
        when (getDiscountPercent()){
            3 -> rg3.isChecked = true
            5 -> rg5.isChecked = true
            7 -> rg7.isChecked = true
            10 -> rg10.isChecked = true
            else -> rgDiscount.clearCheck()
        }
        etResult.setText("Скидка: %.2f".format(calculateDiscount()))
    }

//    Итог = сумма + чаевые - скидка
    private fun calculateTotal(sum: Double, tip: Double, discount: Double): Double{
        return sum + tip - discount
    }



}