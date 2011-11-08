public class CMMASTReturnStatementNode extends CMMASTNode {
  public CMMASTReturnStatementNode (String n, String v, boolean m) { super(n,v,m); }
  public <X,Y> X accept(CMMVisitor<X,Y> visitor, Y data) { return visitor.visit(this, data); }

} // end CMMASTReturnStatementNode
