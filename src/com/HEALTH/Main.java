package com.HEALTH;
import java.util.ArrayList;
import java.util.List;

class InfiniteNumber {
    private List<Integer> internalList = new ArrayList<>();
    private List<Integer> operationsResult = new ArrayList<>();

    public InfiniteNumber(Object inputObject) {
        if (inputObject instanceof Integer) {
            int inputNumber = (int) inputObject;
            if (inputNumber <= 0) {
                throw new IllegalArgumentException("Enter a valid number");
            }
            while (inputNumber != 0) {
                internalList.add(0, inputNumber % 10);
                inputNumber = inputNumber / 10;
            }
        } else if (inputObject instanceof String) {
            String inputString = (String) inputObject;
            for (char c : inputString.toCharArray()) {
                int digit = Character.getNumericValue(c);
                if (digit >= 0) {
                    internalList.add(digit);
                } else {
                    System.out.println("Invalid digit: " + c);
                }
            }
        } else if (inputObject instanceof List) {
            List<?> inputList = (List<?>) inputObject;
            for (Object element : inputList) {
                if (element instanceof Integer) {
                    int digit = (int) element;
                    if (digit < 0 || digit > 9) {
                        throw new IllegalArgumentException("Enter an integer between 0 and 9");
                    }
                    internalList.add(digit);
                } else {
                    throw new IllegalArgumentException("Enter a list of integers");
                }
            }
        } else {
            throw new IllegalArgumentException("Constructor of InfiniteNumber does not support this data type");
        }
    }

    public List<Integer> getInternalList() {
        return internalList;
    }

    public String getNumberAsString(List<Integer> listToString) {
        StringBuilder result = new StringBuilder();
        for (int num : listToString) {
            result.append(num);
        }
        return result.toString();
    }

    public String add(List<Integer> inputList) {
        List<Integer> result = new ArrayList<>();
        int carry = 0;

        int size1 = internalList.size();
        int size2 = inputList.size();
        int maxLength = Math.max(size1, size2);

        for (int i = 0; i < maxLength || carry != 0; i++) {
            int sum = carry;
            if (i < size1) {
                sum += internalList.get(size1 - i - 1);
            }
            if (i < size2) {
                sum += inputList.get(size2 - i - 1);
            }
            result.add(0, sum % 10);
            carry = sum / 10;
        }

        return getNumberAsString(result);
    }

    public String subtract(List<Integer> inputList) {
        List<Integer> result = new ArrayList<>();
        int borrow = 0;

        int size1 = internalList.size();
        int size2 = inputList.size();

        // Ensure that size1 is always greater than or equal to size2
        boolean isNegative = false;
        if (size1 < size2) {
            // Swap the internal list and input list
            List<Integer> temp = internalList;
            internalList = inputList;
            inputList = temp;

            // Swap the sizes
            int tempSize = size1;
            size1 = size2;
            size2 = tempSize;

            // Set a flag to denote that the result will be negative
            isNegative = true;
        }

        for (int i = 0; i < size1 || borrow != 0; i++) {
            int digit1 = (i < size1) ? internalList.get(size1 - i - 1) : 0;
            int digit2 = (i < size2) ? inputList.get(size2 - i - 1) : 0;

            int diff = digit1 - digit2 - borrow;

            if (diff < 0) {
                diff += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }

            result.add(0, diff);
        }

        // Remove leading zeros from the result
        while (result.size() > 1 && result.get(0) == 0) {
            result.remove(0);
        }

        // Add negative sign if necessary
        if (isNegative) {
            result.set(0, -result.get(0));
        }

        return getNumberAsString(result);
    }

    public String multiply(List<Integer> inputList) {
        List<Integer> result = new ArrayList<>();

        // Determine the size of the result
        int size1 = internalList.size();
        int size2 = inputList.size();
        int maxSize = size1 + size2;

        // Create an array to hold the intermediate results
        int[] tempResult = new int[maxSize];

        // Perform multiplication
        for (int i = size1 - 1; i >= 0; i--) {
            for (int j = size2 - 1; j >= 0; j--) {
                int product = internalList.get(i) * inputList.get(j);
                int position1 = i + j;
                int position2 = i + j + 1;
                int sum = product + tempResult[position2];
                tempResult[position1] += sum / 10;
                tempResult[position2] = sum % 10;
            }
        }

        // Construct the result list from the tempResult array
        for (int num : tempResult) {
            result.add(num);
        }

        // Remove leading zeros from the result
        while (result.size() > 1 && result.get(0) == 0) {
            result.remove(0);
        }

        return getNumberAsString(result);
    }

    public String divide(List<Integer> inputList) {
        StringBuilder quotient = new StringBuilder();

        // Convert internalList and inputList to integer values
        int dividend = convertListToInteger(internalList);
        

        int divisor = convertListToInteger(inputList);


        // Handle division by zero
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }

        // Perform division
        int quotientValue = dividend / divisor;
        int remainder = dividend % divisor;

        // Construct quotient and remainder
        quotient.append(quotientValue);
        List<Integer> result = new ArrayList<>();
        result.add(remainder);

        // Handle the case of a non-zero remainder
        if (remainder != 0) {
            quotient.append('.');
            int index = result.size();

            // Perform long division to obtain decimal digits
            while (index < 10) { // Limit decimal places to 10
                remainder *= 10;
                quotientValue = remainder / divisor;
                remainder %= divisor;
                quotient.append(quotientValue);
                result.add(remainder);

                // Break if remainder becomes zero
                if (remainder == 0) {
                    break;
                }
                index++;
            }
        }

        return quotient.toString();
    }

    // Helper method to convert a list of digits to an integer
    private int convertListToInteger(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer digit : list) {
            sb.append(digit);
        }
        if (sb.length() == 0) {
            return 0; // Handle empty list
        }
        return Integer.parseInt(sb.toString());
    }
}

public class Main {
    public static void main(String[] args) {
        List<Integer> array1 = new ArrayList<>();

        array1.add(2);
        array1.add(2);

        List<Integer> array2 = new ArrayList<>();
        array2.add(1);
        array2.add(2);
        array2.add(2);


        InfiniteNumber numberObj = new InfiniteNumber(array1);
        System.out.println(numberObj.subtract(array2));
    }
}