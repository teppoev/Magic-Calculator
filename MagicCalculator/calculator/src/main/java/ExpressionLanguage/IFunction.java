package ExpressionLanguage;

public interface IFunction {
    double Calculate(double[] params);
    int getNumberOfArgs();
}
