import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import android.widget.Button

class FirstFragment : Fragment(R.layout.fragment_first) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button from the fragment layout and set up a click listener
        val button = view.findViewById<Button>(R.id.navigateButton)
        button.setOnClickListener {
            // Navigate to the second fragment when the button is clicked
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }
    }
}


