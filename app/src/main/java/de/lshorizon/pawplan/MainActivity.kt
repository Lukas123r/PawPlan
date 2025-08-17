package de.lshorizon.pawplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import de.lshorizon.pawplan.core.design.PawPlanTheme
import de.lshorizon.pawplan.core.navigation.NavGraph

/**
 * Main activity launching the PawPlan navigation graph.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PawPlanTheme {
                NavGraph()
            }
        }
    }
}
