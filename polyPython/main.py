from sympy import symbols, simplify, sympify

def read_data(file_path):
    data = []
    with open(file_path, 'r') as file:
        lines = file.readlines()
        x_values = list(map(float, lines[0].split()))
        fx_values = list(map(float, lines[1].split()))

        if len(x_values) != len(fx_values):
            raise ValueError("Number of x values and f(x) values must be the same.")

        data = list(zip(x_values, fx_values))
        
    return data

def divided_difference_table(input):
    n = len(input)
    dividedDifferenceTable = [[0] * n for _ in range(n)]
    
    for i in range(n):
        dividedDifferenceTable[i][0] = input[i][1]

    for j in range(1, n):
        for i in range(n - j):
            dividedDifferenceTable[i][j] = (dividedDifferenceTable[i + 1][j - 1] - dividedDifferenceTable[i][j - 1]) / (input[i + j][0] - input[i][0])

    return dividedDifferenceTable

def printLeftTopForm(dividedDifferenceTable):
    for i in range (0, len(dividedDifferenceTable)):
        for j in range (0, len(dividedDifferenceTable[i])):
            print(str(dividedDifferenceTable[i][j]) + " ", end="")
        print("")
    return

def newton_interpolation_polynomial(input, dividedDifferenceTable):
    print("Newton(x) = ", end="")
    result_str = str(dividedDifferenceTable[0][0]);
    length = len(input)

    for i in range(1, length):
        result_str += " + " + str(dividedDifferenceTable[0][i])

        for j in range(0, i):
            result_str += " * ( x - " + str(input[j][0]) + " )"

    print(result_str)
    return result_str

def lagrange_interpolation_polynomial(input, dividedDifferenceTable):
    result_str = "Lagrange(x) = "

    for i in range(0, len(input)):
        term_coef = dividedDifferenceTable[i][0]

        for j in range(0, len(input)):
            if j != i:
                term_coef /= (input[i][0] - input[j][0])
                result_str += "( x - " + str(input[j][1]) + " )"
        
        result_str += " * " + str(term_coef)

        if i < (len(input) - 1):
            result_str += " + "

    print(result_str)
    return result_str

def simplify_interpolation_equation(equation_str):
    equation = sympify(equation_str)
    simplified_equation = simplify(equation)
    return simplified_equation

def main():
    file_path = input("Enter the path of the text file containing x and f(x) values: ")
    data = read_data(file_path)

    table = divided_difference_table(data)
    print("\nDivided Difference Table:")
    printLeftTopForm(table);

    print("\nNewton's Form Polynomial:")
    newton_poly = newton_interpolation_polynomial(data, table)

    print("\nLagrange's Form Polynomial:")
    lagrange_interpolation_polynomial(data, table)

    print("\nSimplified Polynomial:")
    print(simplify_interpolation_equation(newton_poly))
    print("")

if __name__ == "__main__":
    main()
