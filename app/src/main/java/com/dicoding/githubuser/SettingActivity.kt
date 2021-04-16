package com.dicoding.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.dicoding.githubuser.databinding.ActivitySettingBinding
import com.dicoding.githubuser.preference.RemainderPreference

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var remainder: Remainder
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val remainderPreference = RemainderPreference(this)
        binding.switch1.isChecked = remainderPreference.getRemainder().isRemainded

        alarmReceiver = AlarmReceiver()

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveRemainder(true)
                alarmReceiver.setRepeatingAlarm(
                    this, getString(R.string.type_set_repeating_alarm), getString(
                        R.string.time_alarm
                    ), getString(R.string.massage_alarm)
                )
            } else {
                saveRemainder(false)
                alarmReceiver.cancelAlarm(this)
            }
        }
        binding.btnSetLanguage.setOnClickListener(this)

    }

    private fun saveRemainder(state: Boolean) {
        val remainderPreference = RemainderPreference(this)
        remainder = Remainder()

        remainder.isRemainded = state
        remainderPreference.setRemainder(remainder)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_set_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }
}