package ru.urfu.droidpractice1

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import ru.urfu.droidpractice1.databinding.ActivitySecondBinding

private const val PREFS_NAME = "article_prefs"
private const val KEY_READ = "second_article_read"

class SecondActivity : ComponentActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Читаем текущее значение из SharedPreferences
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isRead = prefs.getBoolean(KEY_READ, false)
        binding.switchRead.isChecked = isRead

        // Сохраняем значение при каждом переключении
        binding.switchRead.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean(KEY_READ, checked).apply()

            // Отдаём результат в MainActivity
            setResult(
                Activity.RESULT_OK,
                android.content.Intent().putExtra(KEY_READ, checked)
            )
        }
    }

    override fun onStart()   { super.onStart();   Log.d(TAG, "onStart") }
    override fun onResume()  { super.onResume();  Log.d(TAG, "onResume") }
    override fun onPause()   { super.onPause();   Log.d(TAG, "onPause") }
    override fun onStop()    { super.onStop();    Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }

    companion object { const val TAG = "SecondActivity" }
}