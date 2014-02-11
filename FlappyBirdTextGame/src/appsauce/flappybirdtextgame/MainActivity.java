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
	//flaps to keep track of flaps per game. Just cuz.
	int flaps;
	//gFlaps keep track of flaps between pipes
	int gFlaps;
	//points per round
	int points;
	//hs is the highscore
	int hs;
	//dc is the player's dogecoin
	int dc;
	//restart is set to true if the player clicks >NEW GAME
	boolean restart = false;
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefs = getPreferences(MODE_PRIVATE);
		hs = mPrefs.getInt("hs", 0);
		dc = mPrefs.getInt("dc", 0);

		setContentView(R.layout.activity_main);

		TextView useCoin = (TextView) findViewById(R.id.useCoin);
		useCoin.setVisibility(TextView.GONE);

		if (dc > 0) {
			TextView currentScore = (TextView) findViewById(R.id.dogecoin);
			currentScore.setText("Dogecoins: " + dc);
		}

		if (hs > 0) {
			TextView highScore = (TextView) findViewById(R.id.highScore);
			highScore.setText("High Score: " + hs);
		}

		TextView flap = (TextView) findViewById(R.id.flap);
		flap.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				TextView useCoin = (TextView) findViewById(R.id.useCoin);
				useCoin.setVisibility(TextView.GONE);

				if (restart == true) {
					restart = false;
					TextView button = (TextView) findViewById(R.id.flap);
					button.setText(">FLAP");
					again();
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

			if (dc > 0) {
				TextView useCoin = (TextView) findViewById(R.id.useCoin);
				useCoin.setVisibility(TextView.VISIBLE);

				TextView coin = (TextView) findViewById(R.id.useCoin);
				coin.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
						dc--;
						gFlaps = 1;
						points++;
						TextView currentScore = (TextView) findViewById(R.id.currentScore);
						currentScore.setText("Current Score: " + points);
						TextView useCoin = (TextView) findViewById(R.id.useCoin);
						useCoin.setVisibility(TextView.GONE);
						TextView coins = (TextView) findViewById(R.id.dogecoin);
						coins.setText("Dogecoins: " + dc);
						if (dc == 0) {
							coins.setVisibility(TextView.GONE);
						}
						Editor editor = mPrefs.edit();
						editor.putInt("dc", dc);
						editor.commit();
						payedToll();

					}

				});
			}
			flaps++;
		}
	}

	public void game(int n) {
		if (n == 0) {
			if (hit() == true) {
				die();
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
					} else if (points == 1) {
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
				button.setText(">NEW GAME");
				restart = true;
			} else {
				double r = Math.floor((Math.random() * 10) + 1);
				if (r > 7) {
					getDc();
				} else {
					live();
				}
				points++;
				flaps++;
				gFlaps++;
				TextView currentScore = (TextView) findViewById(R.id.currentScore);
				currentScore.setText("Current Score: " + points);
			}
		} else if (n == 1) {
			TextView gameText = (TextView) findViewById(R.id.gameText);
			gameText.setText("Pipes are nearing again.");
			flaps++;
			gFlaps++;
		} else {
			TextView gameText = (TextView) findViewById(R.id.gameText);
			gameText.setText("Pipes are closer. You flap hard again, trying to fit in a gap.");
			if (dc > 0) {
				TextView useCoin = (TextView) findViewById(R.id.useCoin);
				useCoin.setVisibility(TextView.VISIBLE);

				TextView coin = (TextView) findViewById(R.id.useCoin);
				coin.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
						dc--;
						gFlaps = 1;
						points++;
						TextView currentScore = (TextView) findViewById(R.id.currentScore);
						currentScore.setText("Current Score: " + points);
						TextView useCoin = (TextView) findViewById(R.id.useCoin);
						useCoin.setVisibility(TextView.GONE);
						TextView coins = (TextView) findViewById(R.id.dogecoin);
						coins.setText("Dogecoins: " + dc);
						if (dc == 0) {
							coins.setVisibility(TextView.GONE);
						}
						Editor editor = mPrefs.edit();
						editor.putInt("dc", dc);
						editor.commit();
						payedToll();
					}

				});
			}
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

	public void die() {
		String t;
		double r = Math.floor((Math.random() * 10) + 1);
		if (r < 2) {
			t = "You died.";
		} else if (r < 3) {
			t = "You are shot down by a duck hunter! A dog retrieves your lifeless body.";
		} else if (r < 4) {
			t = "Your weak and wizened body plummets to the ground where you die. Horribly.";
		} else if (r < 5) {
			t = "You\'re getting too old for this sh*t.";
		} else if (r < 6) {
			t = "Do it again. But better.";
		} else if (r < 7) {
			t = "You sank faster than the titanic. You died.";
		} else if (r < 8) {
			t = "You died.";
		} else if (r < 9) {
			t = "You fell into the pipe-well and Lassie is not around to save you. You died.";
		} else {
			t = "You died.";
		}
		TextView gameText = (TextView) findViewById(R.id.gameText);
		gameText.setText(t);
	}

	public void live() {
		String t;
		double r = Math.floor((Math.random() * 10) + 1);
		if (r < 2) {
			t = "You made it through! \n+1 point!";
		} else if (r < 3) {
			t = "Made it! Yolo! +1 point!";
		} else if (r < 4) {
			t = "You\'re only one little bird flitting through life, hold on to what you can and never forget to flap and try again because you might not make it through the next one. \n+1 point!";
		} else if (r < 5) {
			t = "You made it! \n+1 point!";
		} else if (r < 6) {
			t = "Are you having the time of your life? Because you MADE IT! \n+1 point!";
		} else if (r < 7) {
			t = "You made it through! \n+1 point!";
		} else if (r < 8) {
			t = "You made it through! \n+1 point!";
		} else if (r < 9) {
			t = "You made it through! \n+1 point!";
		} else {
			t = "You made it through! \n+1 point!";
		}
		TextView gameText = (TextView) findViewById(R.id.gameText);
		gameText.setText(t);
	}

	public void again() {
		String t;
		double r = Math.floor((Math.random() * 10) + 1);
		if (r < 2) {
			t = "YouÕre playing again?";
		} else if (r < 3) {
			t = "You\'ve now evolved into a flying fish. Flap your way to the ocean where you can be free!";
		} else if (r < 4) {
			t = "You\'re only one little bird flitting through life, hold on to what you can and never forget to flap and try again because you might not make it through the next one. +1 point!";
		} else if (r < 5) {
			t = "You are escaping the law your only way is to ride a bird to safety. How far will you make it before you get caught?";
		} else if (r < 6) {
			t = "You leave your flock. No one understands you. Your lips are just too much for them. But as you leave you come upon a tube forest.";
		} else if (r < 7) {
			t = "FLAP!";
		} else {
			t = "You appear as another strange yellow bird. Your lips are still very fat, yet somehow you manage to stay in mid-air.";
		}
		TextView gameText = (TextView) findViewById(R.id.gameText);
		gameText.setText(t);
	}

	public void payedToll() {
		String t;
		if (hit() == true) {
			t = "A wizard said \"you shall not pass.\" But you paid him off with your coinage. \n+1Point! \n-1 Dogecoin";
		} else {
			t = "You used a Dogecoin and lived! \n+1Point! \n-1 Dogecoin";
		}
		TextView gameText = (TextView) findViewById(R.id.gameText);
		gameText.setText(t);
	}

	public void getDc() {
		TextView gameText = (TextView) findViewById(R.id.gameText);
		gameText.setText("You made it through! \n+1 point! \n \nYou also snagged a rare Dodgecoin! \n+1 Dogecoin! \n\nWOW!");
		dc++;

		TextView currentScore = (TextView) findViewById(R.id.dogecoin);
		currentScore.setText("Dogecoins: " + dc);
		currentScore.setVisibility(View.VISIBLE);

		Editor editor = mPrefs.edit();
		editor.putInt("dc", dc);
		editor.commit();
	}
}
