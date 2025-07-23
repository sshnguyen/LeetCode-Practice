# Part 1: Basic Reference Resolution
# Given a target variable and a list of variable definitions, calculate the target's value. 
# Variables can be assigned either direct values or references to other variables.
# Target: T3
# Expressions: [T1=1,T2=2,T3=T4,T4=T5,T5=T2]
# Output: 2

#✅✅✅✅
#CLARIFIYING QUESTIONS
#can we assume that the value assignments can only be integers?, can it be negative?
#format of the expression are always syntactically correct? no spaces... only one =, ect.
#do we have to handle cycles leading to infinite loop?

def evaluatePart1(target, expressions):
    mapping = {}
    
    # Build variable -> value/reference map
    for expr in expressions:
        var, val = expr.split('=')
        mapping[var] = val

    # Follow the reference chain until you get a direct value
    #keep updating the target until the value of target reference is a positive
    while not (mapping[target].isdigit()):
        target = mapping[target]

    return int(mapping[target])

def evaluatePart2(target, expressions):
    mapping = {}
    
    # Build variable -> value/reference map
    for expr in expressions:
        var, val = expr.split('=')
        mapping[var] = val

    # Follow the reference chain until you get a direct value
    #keep updating the target until the value of target reference is a digit or negative digit.
    while True:
        val = mapping[target]

        # Direct number (positive or negative)
        if val.isdigit():
            return int(val)

        # Expression with + or -
        if '+' in val or '-' in val[1:]:  # skip leading minus
            op_index = val.find('+')
            if op_index == -1:
                op_index = val.find('-', 1)

            left = val[:op_index]
            right = val[op_index + 1:]
            op = val[op_index]

            # Resolve left operand
            while not left.isdigit():
                left = mapping[left]
            # Resolve right operand
            while not right.isdigit():
                right = mapping[right]

            left_val = int(left)
            right_val = int(right)

            return left_val + right_val if op == '+' else left_val - right_val

        # Reference to another variable
        target = val

def evaluatePart3(target, expressions):
    mapping = {}
    for expr in expressions:
        var, val = expr.split('=')
        mapping[var] = val

    visited = set()

    while True:
        if target in visited:
            return "IMPOSSIBLE"
        visited.add(target)

        val = mapping[target]

        # Direct number
        if val.isdigit():
            return int(val)

        # Expression with + or - (only one operator allowed)
        if '+' in val or '-' in val[1:]:  # skip leading minus
            op_index = val.find('+')
            if op_index == -1:
                op_index = val.find('-', 1)

            left = val[:op_index]
            right = val[op_index + 1:]
            op = val[op_index]

            # Resolve left operand fully (chain of references)
            left_visited = set()
            while not left.isdigit():
                if left in left_visited or left in visited:
                    return "IMPOSSIBLE"
                left_visited.add(left)
                left = mapping[left]

            # Resolve right operand fully (chain of references)
            right_visited = set()
            while not right.isdigit():
                if right in right_visited or right in visited:
                    return "IMPOSSIBLE"
                right_visited.add(right)
                right = mapping[right]

            left_val = int(left)
            right_val = int(right)

            return left_val + right_val if op == '+' else left_val - right_val

        # Reference to another variable
        target = val



if __name__ == "__main__":
  target = "T3"
  expressions = ["T1=","T2=2","T3=T4","T4=T5","T5=T2"]
  result = evaluatePart1(target, expressions)
  # Optional: assert expected output for testing
  expected = 2
  assert expected == result, "Test failed! Part 1"

  target = "T3"
  expressions = ["T1=1","T2=2","T3=T4+T5","T4=T1","T5=T2"]
  result = evaluatePart2(target, expressions)
  print(result)
  # Optional: assert expected output for testing
  expected = 3
  assert expected == result, "Test failed! Part 2"

  target = "T3"
  expressions = ["T1=1","T2=2","T3=T4+T5","T4=T1","T5=T3"]
  result = evaluatePart3(target, expressions)
  # Optional: assert expected output for testing
  expected = "IMPOSSIBLE"
  assert expected == result, "Test failed! Part 2"


  print("Test passed!")

