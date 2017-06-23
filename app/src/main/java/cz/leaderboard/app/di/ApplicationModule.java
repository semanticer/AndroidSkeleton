package cz.leaderboard.app.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    static LeaderboardRepository provideLeaderboardRepository(FirebaseDatabase firebaseDatabase){
        return new FirebaseLeaderboardRepository(firebaseDatabase);
    }

    @Provides
    static FirebaseDatabase provideFirebaseDatabase(){
        return FirebaseDatabase.getInstance();
    }

    @Provides
    static FirebaseUser provideFirebaseUser(FirebaseAuth firebaseAuth){
        return firebaseAuth.getCurrentUser();
    }

    @Provides
    static FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

}
