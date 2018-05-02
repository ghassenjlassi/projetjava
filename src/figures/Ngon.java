package figures;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;

import figures.enums.FigureType;

/**
 * Classe de Rectangle pour les {@link Figure}
 *
 * @author davidroussel
 */
public class Ngon extends Figure
{
	/**
	 * Le compteur d'instance des cercles.
	 * Utilisé pour donner un numéro d'instance après l'avoir incrémenté
	 */
	private static int counter = 0;
	private Point2D center; 
	double rayon;
	int n;

	/**
	 * Création d'un rectangle avec les points en haut à gauche et en bas à
	 * droite
	 *
	 * @param on peut stroker le type de trait
	 * @param on fait la edge de la couleur du traitement
	 * @param on fill la couleur de remplissage
	 * @param topLeft le point en haut à gauche
	 * @param bottomRight le point en bas à droite
	 * @param 
	 */
	
	public Ngon(BasicStroke stroke, Paint edge, Paint fill, Point2D topg,
			Point2D bottomRight)
	{
		super(stroke, edge, fill);
		
		instanceNumber = ++counter;
		
		double x = topg.getX();
		
		double y = topg.getY();
		
		rayon=(bottomRight.getY() - y);
		
		n=(int) (Math.abs(bottomRight.getX() - x)*2/rayon)+1;
		
		shape=createNgon(x,y,rayon,n);
		
	}
	static Path2D createNgon(double x,double y,double rayon,int n){
		double startAngleRad=Math.toRadians(-18);
		
		int numRays=n;
		
		Path2D star = new Path2D.Double();
        
		double deltaAngleRad = Math.PI / numRays;
        
		for (int i = 0; i < numRays * 2; i++)
        {
            double angleRad = startAngleRad + i * deltaAngleRad;
        
            double ca = Math.cos(angleRad);
            
            double sa = Math.sin(angleRad);
            
            double relX = ca;
            
            double relY = sa;
            
            relX *= rayon;
            
            relY *= rayon;
            
            if (i == 0)
            {
                ((Path2D) star).moveTo(x + relX, y + relY);
            }
            else
            {
                ((Path2D) star).lineTo(x + relX, y + relY);
            }
        }
        ((Path2D) star).closePath();
        return star;
	}

	/**
	 * j'ai pens� a faire un constructeur de copie qui assure une copie distincte du rectangle
	 * @param rectangle le rectangle à copier
	 */
	public Ngon(Ngon rect)
	{
		super(rect);
		
		Path2D oldrectangle = (Path2D) ((Path2D) rect.shape).clone();
		
		shape=oldrectangle;
	}

	/**
	 * Création d'une copie distincte de la figure
	 * @see figures.Figure#clone()
	 */
	@Override
	public Figure clone()
	{
		return new Ngon(this);
	}

	/**
	 * Création d'un rectangle sans points (utilisé dans les classes filles
	 * pour initialiser seulement les couleur et le style de trait sans
	 * initialiser {@link #shape}.
	 *
	 * @param on stroke le type de trait
	 * @param on edge la couleur du trait
	 * @param on fill la couleur de remplissage
	 */
	protected Ngon(BasicStroke stroke, Paint edge, Paint fill)
	{
		super(stroke, edge, fill);

		shape = null;
	}

	/**
	 * Déplacement du point en bas à droite du rectangle à la position
	 * du point p
	 *
	 * @param p la nouvelle position du dernier point
	 * @see figures.Figure#setLastPoint(Point2D)
	 */
	
	public void setLastPoint(Point last,Point start)
	{
		if (shape != null)
		{	
			n=(int) (Math.abs(last.getX()-start.getX())*2/(last.getY()-start.getY()))+1;
			
			rayon=last.getY()-start.getY();
			
			shape=createNgon(start.getX(),start.getY(),rayon,n);
			
		}
		else
		{
			System.err.println(getClass().getSimpleName() + "::setLastPoint : null shape");
		}
	}

	/**
	 * on prend le barycentre de la figure.
	 * @return on fait le  retour du point correspondant au barycentre de la figure
	 */
	@Override
	public Point2D getCenter()
	{
		RectangularShape rect =  shape.getBounds();
		
		Point2D center = new Point2D.Double(rect.getCenterX(), rect.getCenterY());
		
		return center;
	}

	/**
	 * Normalise une figure de manière à exprimer tous ses points par rapport
	 * à son centre, puis transfère la position réelle du centre dans l'attribut
	 * {@link #translation}
	 */
	@Override
	public void normalize()
	{	
		Point2D center = getCenter();
		
		double cx = center.getX();
		
		double cy = center.getY();
		
		System.out.println(cx);
		
		translation.translate(cx, cy);
		
		shape= createNgon(0,0,rayon,n);
	}

 	/**
 	 * accesseur du  type de figure selon {@link FigureType}
 	 * @return retour du type de figure
 	 */
 	@Override
	public FigureType getType()
 	{
 		return FigureType.NGON;
 	}
	@Override
	//modifier le dernier point 
	
	public void setLastPoint(Point2D p) {
		
		
	}
}
