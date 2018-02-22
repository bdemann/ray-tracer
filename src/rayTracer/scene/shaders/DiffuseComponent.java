package rayTracer.scene.shaders;

public class DiffuseComponent extends ShaderComponent {
	private Color surfaceColor;
	private Color highlightColor;
	private int phongConst;
	
	public DiffuseComponent(Color diffuseColor, Color highlightColor, int phongConst) {
		super();
		this.surfaceColor = diffuseColor;
		this.highlightColor = highlightColor;
		this.phongConst = phongConst;
	}

	public Color getDiffuse() {
		return surfaceColor;
	}

	public void setColor(Color color) {
		this.surfaceColor = color;
	}

	public Color getSpec() {
		return highlightColor;
	}

	public void setHighlight(Color highlight) {
		this.highlightColor = highlight;
	}

	public int getPhongConst() {
		return phongConst;
	}

	public void setPhongConst(int phongConst) {
		this.phongConst = phongConst;
	}
}
