import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gettasksdone.gettasksdone.R
import com.gettasksdone.gettasksdone.data.JwtHelper
import com.google.android.material.navigation.NavigationView

object NavHeaderUtils {
    fun updateNavHeader(activity: AppCompatActivity) {
        val jwtHelper = JwtHelper(activity)
        val navView: NavigationView = activity.findViewById(R.id.nav_view)
        val navHeaderView = LayoutInflater.from(activity).inflate(R.layout.nav_header_menu, null)
        val usuarioTextView = navHeaderView.findViewById<TextView>(R.id.usuario)
        usuarioTextView.text = jwtHelper.getUsername()
        navView.removeHeaderView(navView.getHeaderView(0))
        navView.addHeaderView(navHeaderView)

    }
}
