package ie.wit.landmark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import ie.wit.landmark.main.LandmarkApp
import ie.wit.landmark.models.LandmarkModel
import landmark.R
import landmark.databinding.ActivityLandmarkBinding
import timber.log.Timber

class Donate : AppCompatActivity() {

    private lateinit var landmarkLayout : ActivityLandmarkBinding
    lateinit var app: LandmarkApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        landmarkLayout = ActivityLandmarkBinding.inflate(layoutInflater)
        setContentView(landmarkLayout.root)
        app = this.application as LandmarkApp

        landmarkLayout.progressBar.max = 10000

        landmarkLayout.amountPicker.minValue = 1
        landmarkLayout.amountPicker.maxValue = 1000

        landmarkLayout.amountPicker.setOnValueChangedListener { _, _, newVal ->
            //Display the newly selected number to paymentAmount
            landmarkLayout.paymentAmount.setText("$newVal")
        }

        var totalDonated = 0

        landmarkLayout.landmarkButton.setOnClickListener {
            val amount = if (landmarkLayout.paymentAmount.text.isNotEmpty())
                landmarkLayout.paymentAmount.text.toString().toInt() else landmarkLayout.amountPicker.value
            if(totalDonated >= landmarkLayout.progressBar.max)
                Toast.makeText(applicationContext,"Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            else {
                val paymentmethod = if(landmarkLayout.paymentMethod.checkedRadioButtonId == R.id.Direct)
                    "Direct" else "Paypal"
                totalDonated += amount
                landmarkLayout.totalSoFar.text = "$$totalDonated"
                landmarkLayout.progressBar.progress = totalDonated
                app.landmarksStore.create(LandmarkModel(paymentmethod = paymentmethod,amount = amount))
                Timber.i("Total Donated so far $totalDonated")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_landmark, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_report -> { startActivity(Intent(this,
                Report::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}