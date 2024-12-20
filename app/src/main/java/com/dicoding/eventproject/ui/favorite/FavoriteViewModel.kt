import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.eventproject.database.EventDatabase
import com.dicoding.eventproject.database.FavoriteEventEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(context: Context) : ViewModel() {
    private val dao = EventDatabase.getDatabase(context).favoriteEventDao()

    val favoriteEvents: LiveData<List<FavoriteEventEntity>> = dao.getAllFavoriteEvents()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    init {
        fetchFavoriteEvents()
    }

    private fun fetchFavoriteEvents() {
        _loadingState.value = true
        viewModelScope.launch {
            dao.getAllFavoriteEvents().observeForever {
                _loadingState.value = false
            }
        }
    }

    fun addFavoriteEvent(event: FavoriteEventEntity) {
        viewModelScope.launch {
            dao.addFavoriteEvent(event)
        }
    }

    fun removeFavoriteEvent(eventId: Int) {
        viewModelScope.launch {
            dao.removeFavoriteEvent(eventId)
        }
    }

    fun getFavoriteEventById(eventId: Int): LiveData<FavoriteEventEntity?> {
        return dao.getFavoriteEventById(eventId)
    }
}
