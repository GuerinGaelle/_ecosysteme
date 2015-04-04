import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WaterAgent extends Agent
{
	static int nbW= 0;
	int tsunamiIt=1;

	public WaterAgent(int __x, int __y, World __w)
	{
		super(__x, __y, __w);
		nbW++;
		try
		{
			img = ImageIO.read(new File("WaterAgent.png"));
		}
		catch (Exception e)
		{
			System.out.println("WaterAgent : sprite not found");
			System.exit(-1);
		}
	}

	public void step(int place)
	{
		if (PV <= 0) //|| (age == 100))
		{
			PV = 0;
			nbW--;
			_alive = false;
		}
		if (_alive)
		{
			age++;
			attaque_alentour(place);
			repere_environement();
			deplacement();
		}
		else
		{
			try
			{
				img = ImageIO.read(new File("deathwater.png"));
			}
			catch (Exception e)
			{
				System.out.println("deathwater : sprite not found");
				System.exit(-1);
			}
		}
	}

	void repere_environement()
	{
		if (!_alive)
			return;
		if (_world.getCellState(_x, _y)[3] || _world.getCellState(_x, _y)[5])
		{
			_alive = false;
			nbW--;
			return;
		}

		if ((_world.getCellState(_x, _y)[4])&&_world.explosion)
		{
			_world.tsunami= true;
			_world.waterFlood(_x, _y, tsunamiIt, _world.explosion);
			tsunamiIt++;
		}
		if ( !_world.explosion && (tsunamiIt!=1) ) {
			_world.tsunami= false;
			_world.waterFlood(_x, _y, tsunamiIt, _world.explosion);
			tsunamiIt--;
		}
	}

	void attaque_alentour (int place)
	{
		if (!_alive)
			return;
		int j = 0;
		for (int i = 0; i != _world.agents.size(); i += 1)
		{
			Agent a = _world.agents.get(i);
			if ((a._x == _x) && (a._y == _y) && (a instanceof FireAgent) && (a._alive = true))
				if ((float)Math.random() <= 0.15)PV = PV - 10;
			if ((a._x == _x) && (a._y == _y) && (a instanceof EarthAgent) && (a._alive = true) )
			{
				PV = PV - 20;
				if ((float)Math.random() <= 0.85)PV = PV - 50;
			}
			if ((a._x == _x) && (a._y == _y) && (a instanceof WaterAgent) && (place != i)
			        && (a._alive = true) && (age < 40) && (age >= 10)&&
					(a.age < 40) && (a.age >= 10) && (a.PV > 20) && (PV > 20)&&(nbW<6))
			{
				//reproduction = 1;
				boolean test = false;
				while ((j < _world.agents.size()) && (test == false))
				{
					Agent b = _world.agents.get(j);
					if ((b instanceof WaterAgent) && !a._alive)
					{
						b = new WaterAgent(_x, _y, _world);
						test = true;
					}
					j++;
				}
				if (!test)
					_world.add(0);
			}
		}

	}

	void deplacement ()
	{
		if ((!_alive)||((_world.getCellState(_x, _y)[4])&&_world.explosion)||(! _world.explosion && (tsunamiIt!=1)))
			return;
		_orient = (int)(Math.random() * 4);

		switch ( _orient )
		{
		case 0: // nord
			if ((_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[1] == true)
			        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[3] == true)
			        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[5] == true))
				break;

			_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
			break;


		case 1: // est
				if ((_world.getCellState( ( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[1] == true)
			        || (_world.getCellState( ( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[3] == true)
			        || (_world.getCellState( ( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[5] == true))
				break;

			_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
			break;


		case 2: // sud
			if ((_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[1] == true)
			        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[3] == true)
			        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[5] == true))
				break;

			_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
			break;


		case 3: // ouest
			if ((_world.getCellState( ( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[1] == true)
			        || (_world.getCellState( ( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[3] == true)
			        || (_world.getCellState( ( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[5] == true))
				break;

			_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
			break;
		}

	}
}
