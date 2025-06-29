package com.nestor.cinerama.desing.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.nestor.cinerama.databinding.StartCarruselBinding
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.model.CarouselType

class MainHomeFragment : Fragment() {

    private var _binding: StartCarruselBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartCarruselBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeCarousel()
    }

    private fun initializeCarousel() {
        val list = listOf(
            CarouselItem(
                "https://www.contigoalcine.com/assets/peliculas/DEEPWEB-SHOW-MORTAL-F6.jpg",
                "Pelicula 1"
            ),
            CarouselItem(
                "https://upload.wikimedia.org/wikipedia/en/b/ba/Beetlejuice_Beetlejuice_poster.jpg",
                "Pelicula 2"
            ),
            CarouselItem(
                "https://www.bfdistribution.cl/wp-content/uploads/2024/06/POSTER-WEB-1.jpg",
                "Pelicula 3"
            ),
            CarouselItem(
                "https://www.americatv.com.pe/cinescape/wp-content/uploads/2024/08/666b10d032ae3c6e8c614cf40-210x300.jpg",
                "Pelicula 4"
            )
        )

        binding.carrucel.apply {
            registerLifecycle(lifecycle) // Importante para que el autoplay funcione correctamente

            carouselType = CarouselType.BLOCK
            showIndicator = true
            autoPlay = true
            autoPlayDelay = 3000
            setData(list)
        }
    }

    /*
    // Ejemplo para manejar clics en cada ítem del carrusel
    private fun handleCarouselItemClick(position: Int) {
        val activity = activity ?: return
        val targetActivity = when (position) {
            0 -> FirstActivity::class.java
            1 -> SecondActivity::class.java
            2 -> ThirdActivity::class.java
            3 -> FourthActivity::class.java
            else -> null
        }
        targetActivity?.let {
            val intent = Intent(activity, it)
            startActivity(intent)
        }
    }
    */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}