package cz.leaderboard.app.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.database.FirebaseDatabase;

import cz.leaderboard.app.App;
import cz.leaderboard.app.data.repository.FirebaseLeaderboardRepository;
import cz.leaderboard.app.domain.LeaderboardRepository;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by tomas.valenta on 5/11/2017.
 */

@Module
abstract class ApplicationModule {
    @Binds
    abstract Application application(App app);

    @Provides
    static SharedPreferences preferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    static LeaderboardRepository provideLeaderboardRepository(FirebaseDatabase firebaseDatabase, SharedPreferences sharedPreferences){
        return new FirebaseLeaderboardRepository(firebaseDatabase, sharedPreferences);
    }

    @Provides
    static FirebaseDatabase provideFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }

}
