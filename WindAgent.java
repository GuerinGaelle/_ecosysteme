import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class WindAgent extends Agent
{

	public WindAgent(int __x, int __y, World __w)
	{
		super(__x, __y, __w);
		try
		{
			img = ImageIO.read(new File("WindAgent.png"));
		}
		catch (Exception e)
		{
			System.out.println("WindAgent : sprite not found");
			System.exit(-1);
		}
	}

	public void step (int place)
	{
		if (_alive)
		{
			/*PV = PV - 1;
			if (reproduction == 20)
				reproduction = 0;
			else if (reproduction >= 1)
				reproduction++;*/
			age++;
			attaque_alentour(place);
			if ((PV <= 0)|| (age==100))
				_alive = false;
			repere_environement();
			deplacement();
		}
		else
		{
			try
			{
				img = ImageIO.read(new File("deathwind.png"));
			}
			catch (Exception e)
			{
				System.out.println("deathwind : sprite not found");
				System.exit(-1);
			}
		}
	}

	void repere_environement()
	{
		if(!_alive)
			return;

	}

	void attaque_alentour (int place)
	{
		if(!_alive)
			return;
		int j = 0;
		for (int i = 0; i != _world.agents.size(); i += 1)
		{
			Agent a = _world.agents.get(i);
			/*if((a._x == _x) && (a._y == _y) && (a instanceof WaterAgent) && (a._alive=true)) {
			    if((float)Math.random()<=0.50)PV=PV-10;*/
			if ((a._x == _x) && (a._y == _y) && (a instanceof FireAgent) && (a._alive = true))
			{
				PV = PV - 20;
				if ((float)Math.random() <= 0.85)PV = PV - 50;
			}
			if ((a._x == _x) && (a._y == _y) && (a instanceof EarthAgent) && (a._alive = true) )
				if ((float)Math.random() <= 0.15)PV = PV - 10;
			if ((a._x == _x) && (a._y == _y) && (a instanceof WindAgent) && (place != i) && (a._alive = true) && (age<40) && (age>=10) && (PV>20))
			{
				//reproduction = 1;
				boolean test = false;
				while ((j < _world.agents.size()) && (test == false))
				{
					Agent b = _world.agents.get(j);
					if ((b instanceof WindAgent) && !a._alive)
					{
						b = new WindAgent(_x, _y, _world);
						test = true;
					}
					j++;
				}
				if (!test)
					_world.add(3);
			}
		}

	}

	void deplacement ()
	{
		if(!_alive)
			return;
		_orient = (int)(Math.random() * 4);
		switch ( _orient ){
		
		case 0: // nord
			_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
			break;
			
		case 1: // est
			_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
			break;

		case 2: // sud
			_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
			break;

		case 3: // ouest
			_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
			break;
		}

	}
}
