# Utility to read a csv and get a specified column of data written to a new file


class ColumnReader:
    def __init__(self, input_file="test.csv", output_file="tmp.csv", usecols=[1], sep=",", header=0):
        import pandas as pd
        import os
        path = os.getcwd() + "\\"
        # print(input_file)
        # print(path + input_file)
        columns = []; columns.append(usecols)
        tmp = pd.read_csv(path + input_file, sep=sep, header=header, usecols=columns)
        print(tmp)
        tmp.to_csv(path + output_file, index=False, header=False)

if __name__ == "__main__":
    import sys
    print(sys.argv, len(sys.argv))
    test = ColumnReader(sys.argv[1], sys.argv[2], int(sys.argv[3]))
