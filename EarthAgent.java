import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EarthAgent extends Agent
{
	boolean enterre;
	public EarthAgent(int __x, int __y, World __w)
	{
		super(__x, __y, __w);
		enterre=false;
		try
		{
			img = ImageIO.read(new File("EarthAgent.png"));
		}
		catch (Exception e)
		{
			System.out.println("Earth Agent : sprite not found");
			System.exit(-1);
		}
	}

	public void step(int place)
	{
		if (enterre && _alive){
			deterre();
			return;
		}
		else{
			if (_alive)
		{
			/*if (reproduction == 20)
				reproduction = 0;
			else if (reproduction >= 1)
				reproduction++;*/
			//PV = PV - 1;
			age++;
			deplacement();
			repere_environement();
			attaque_alentour(place);
			if ((PV <= 0)|| (age==100))
			_alive = false;
			}
		else
		{
			try
			{
				img = ImageIO.read(new File("deathearth.png"));
			}
			catch (Exception e)
			{
				System.out.println("deathearth : sprite not found");
				System.exit(-1);
			}
		}
	}
	}

	void repere_environement()
	{
		if(!_alive)
			return;
		if ((_world.getCellState(_x, _y)[4])||_world.getCellState(_x, _y)[3]||_world.getCellState(_x, _y)[5]){
			_alive=false;
			return;
		}
		if (_world.explosion)
			if ((_world.getCellState(_x, _y-1)[1]||_world.getCellState(_x, _y-1)[3]
				||_world.getCellState(_x, _y-1)[4] || _world.getCellState(_x, _y-1)[5])
				&&(_world.getCellState(_x, _y+1)[1]||_world.getCellState(_x, _y+1)[3]
				||_world.getCellState(_x, _y+1)[4] || _world.getCellState(_x, _y+1)[5])
				&&(_world.getCellState(_x-1, _y)[1]||_world.getCellState(_x-1, _y)[3]
				||_world.getCellState(_x-1, _y)[4] || _world.getCellState(_x-1, _y)[5])
				&&(_world.getCellState(_x+1, _y)[1]||_world.getCellState(_x+1, _y)[3]
				||_world.getCellState(_x+1, _y)[4] || _world.getCellState(_x+1, _y)[5])){
					enterre=true;
					return;		
			}

		if (_world.getCellState(_x, _y)[2]){
			_world.setCellState(2, false,_x, _y);
			_world.setCellState(0, true, _x, _y);
			
		}

	}
	
	void deterre(){
		if (!((_world.getCellState(_x, _y-1)[1]||_world.getCellState(_x, _y-1)[3]
				||_world.getCellState(_x, _y-1)[4] || _world.getCellState(_x, _y-1)[5])
				&&(_world.getCellState(_x, _y+1)[1]||_world.getCellState(_x, _y+1)[3]
						||_world.getCellState(_x, _y+1)[4] || _world.getCellState(_x, _y+1)[5])
				&&(_world.getCellState(_x-1, _y)[1]||_world.getCellState(_x-1, _y)[3]
						||_world.getCellState(_x-1, _y)[4] || _world.getCellState(_x-1, _y)[5])
				&&(_world.getCellState(_x+1, _y)[1]||_world.getCellState(_x+1, _y)[3]
						||_world.getCellState(_x+1, _y)[4] || _world.getCellState(_x+1, _y)[5])
	
				)){
			enterre=false;
			return;		
		}
		
	}

	void attaque_alentour (int place)
	{
		if(!_alive)
			return;
		int j = 0;
		for (int i = 0; i != _world.agents.size(); i += 1)
		{
			Agent a = _world.agents.get(i);
			if ((a._x == _x) && (a._y == _y) && (a instanceof WaterAgent) && (a._alive = true) )
				if ((float)Math.random() <= 0.15)
					PV = PV - 10;
			if ((a._x == _x) && (a._y == _y) && (a instanceof WindAgent) && (a._alive = true))
			{
				PV = PV - 20;
				if ((float)Math.random() <= 0.85)
					PV = PV - 50;
			}
			if ((a._x == _x) && (a._y == _y) && (a instanceof FireAgent) && (a._alive = true))
				if ((float)Math.random() <= 0.50)
					PV = PV - 30;
			// si un agent de terre se trouve au meme endroit que lui, que
			if ((a._x == _x) && (a._y == _y) && (a instanceof EarthAgent) && (place != i) && (a._alive = true) && (age<40)&&(age>=10)&&(PV>20))
			{
				//reproduction = 1;
				boolean test = false;
				while ((j < _world.agents.size()) && (test == false))
				{
					Agent b = _world.agents.get(j);
					if ((b instanceof EarthAgent) && !a._alive)
					{
						b = new EarthAgent(_x - (int)(Math.random()*3 - 1), _y, _world);
						test = true;
					}
					j++;
				}
				if (!test)
					_world.add(1);
			}
		}

	}
	
	void deplacement ()
	{
		if(!_alive)
			return;
		_orient = (int)(Math.random() * 4);

		switch ( _orient )
		{
		case 0: // nord
			/*if (Math.abs(_world.alt[_x][_y] - _world.alt[_x][( _y - 1 + _world.getHeight() ) % _world.getHeight()]) >= 2)
				break;*/

			if ((_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[0] == false)
				&&(_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[2] == false))
				break;

			_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
			break;


		case 1: // est
			/*if (Math.abs(_world.alt[_x][_y] - _world.alt[( _x + 1 + _world.getWidth() ) % _world.getWidth()][_y]) >= 2)
				break;*/

			if ((_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[0] == false)
					&&(_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[2] == false))
					break;

			_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
			break;


		case 2: // sud
			/*if (Math.abs(_world.alt[_x][_y] - _world.alt[_x][( _y + 1 + _world.getHeight() ) % _world.getHeight()]) >= 2)
				break;*/

			if ((_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[0] == false)
					&&(_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[2] == false))
					break;

			_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
			break;


		case 3: // ouest
			/*if (Math.abs(_world.alt[_x][_y] - _world.alt[( _x - 1 + _world.getWidth() ) % _world.getWidth()][_y]) >= 2)
				break;*/

			if ((_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[0] == false)
					&&(_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[2] == false))
					break;

			_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
			break;
		}
	}
}
