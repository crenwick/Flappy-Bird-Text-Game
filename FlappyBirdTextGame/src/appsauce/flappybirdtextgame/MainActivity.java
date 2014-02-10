package appsauce.flappybirdtextgame;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	int flaps;
	int gFlaps;
	int points;
	int hs;
	boolean restart = false;
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefs = getPreferences(MODE_PRIVATE);
		hs = mPrefs.getInt("hs", 0);

		setContentView(R.layout.activity_main);

		if (hs > 0) {
			TextView highScore = (TextView) findViewById(R.id.highScore);
			highScore.setText("High Score: " + hs);
		}

		TextView flap = (TextView) findViewById(R.id.flap);
		flap.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				if (restart == true) {
					restart = false;
					TextView button = (TextView) findViewById(R.id.flap);
					button.setText(">FLAP");
					TextView gameText = (TextView) findViewById(R.id.gameText);
					gameText.setText("You appear as another strange yellow bird. Your lips are still very fat, yet somehow you manage to stay in mid-air.");
					TextView currentScore = (TextView) findViewById(R.id.currentScore);
					currentScore.setText("Current Score: " + points);
				} else {
					if (flaps < 3) {
						newGame(flaps);
					} else {
						game(gFlaps);
					}
				}
			}

		});
	}

	private void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void newGame(int n) {
		if (n == 0) {
			TextView gameText = (TextView) findViewById(R.id.gameText);
			gameText.setText("You flap your wings, hurling you to the bright blue sky. But you quickly begin to dive down, falling into the green grass below.");
			flaps++;
		} else if (n == 1) {
			TextView gameText = (TextView) findViewById(R.id.gameText);
			gameText.setText("You flap back up again, and you notice two green pipes up ahead.");
			flaps++;
		} else {
			TextView gameText = (TextView) findViewById(R.id.gameText);
			gameText.setText("As the pipes grow closer, you flap hard, trying to fit in a gap.");
			flaps++;
		}
	}

	public void game(int n) {
		if (n == 0) {
			if (hit() == true) {
				TextView gameText = (TextView) findViewById(R.id.gameText);
				gameText.setText("You died.");
				TextView currentScore = (TextView) findViewById(R.id.currentScore);
				currentScore.setText("Current Score: " + points);
				if (points > 0 && points > hs) {
					hs = points;

					Editor editor = mPrefs.edit();
					editor.putInt("hs", hs);
					editor.commit();

					TextView highScore = (TextView) findViewById(R.id.highScore);
					highScore.setText("High Score: " + hs);

					String m;
					if (hs > 1) {
						m = "NEW HIGH SCORE! " + hs + " POINTS!";
					} else {
						m = "NEW HIGH SCORE! " + hs + " POINT!";
					}
					toast(m);
				} else {
					String m;
					if (points == 0) {
						m = "You scored zero points.";
					} else if (points == 1){
						m = "You scored " + points + " point!";
					} else {
						m = "You scored " + points + " points!";
					}
					toast(m);
				}
				
				flaps = 0;
				gFlaps = 0;
				points = 0;
				TextView button = (TextView) findViewById(R.id.flap);
				button.setText("NEW GAME");
				restart = true;
			} else {
				points++;
				TextView gameText = (TextView) findViewById(R.id.gameText);
				gameText.setText("You made it through! +1 point!");
				TextView currentScore = (TextView) findViewById(R.id.currentScore);
				currentScore.setText("Current Score: " + points);
				flaps++;
				gFlaps++;
			}
		} else if (n == 1) {
			TextView gameText = (TextView) findViewById(R.id.gameText);
			gameText.setText("Pipes are nearing again.");
			flaps++;
			gFlaps++;
		} else {
			TextView gameText = (TextView) findViewById(R.id.gameText);
			gameText.setText("Pipes are closer. You flap hard again, trying to fit in a gap.");
			flaps++;
			gFlaps = 0;
		}
	}

	public boolean hit() {
		Random r = new Random();
		return r.nextBoolean();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.more_apps:
			appSauce(null);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void appSauce(View v) {
		String url = "https://play.google.com/store/apps/developer?id=App+Sauce+Co.";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
}
