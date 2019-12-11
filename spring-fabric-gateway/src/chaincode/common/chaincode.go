/*
 * CommonContract
 */

package main

/* Imports
 * 4 utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
 * 2 specific Hyperledger Fabric specific libraries for Common Contracts
 */
import (
	"bytes"
	"fmt"
	"strconv"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"
)

// CommonContract Define the Common Contract structure, it stores the structure of tracing objects.
type CommonContract struct {
}

/*
 * The Init method is called when the Common Contract is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
func (s *CommonContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	function, args := APIstub.GetFunctionAndParameters()
	if function == "init" {
		return s.create(APIstub, args);
	}
	return shim.Success([]byte("Chaincode say 'hi' to you"))
}

/*
 * The Invoke method is called as a result of an application request to run the Common Contract
 * The calling application program has also specified the particular Common contract function to be called, with arguments
 */
func (s *CommonContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {

	// Retrieve the requested Common Contract function and arguments
	function, args := APIstub.GetFunctionAndParameters()
	// Route to the appropriate handler function to interact with the ledger appropriately
	if function == "create" {
		return s.create(APIstub, args)
	} else if function == "get" {
		return s.get(APIstub, args)
	} else if function == "update" {
		return s.update(APIstub, args)
	} else if function == "delete" {
		return s.delete(APIstub, args)
	} else if function == "list" {
		return s.list(APIstub, args)
	} else if function == "query" {
		return s.query(APIstub, args)
	} else if function == "count" {
		return s.count(APIstub, args)
	} else if function == "exists" {
		return s.exists(APIstub, args)
	} else if function == "history" {
		return s.history(APIstub, args)
	}
	return shim.Error("Unsupport function " + function)
}

func (s *CommonContract) history(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	class := args[0]
	key := args[1]

	objectType := "type~key"
	compositeKey, err := APIstub.CreateCompositeKey(objectType, []string{class, key})
	if err != nil {
		return shim.Error(err.Error())
	}

	resultsIterator, err := APIstub.GetHistoryForKey(compositeKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing historic values for the marble
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		response, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"txId\":")
		buffer.WriteString("\"")
		buffer.WriteString(response.TxId)
		buffer.WriteString("\"")

		buffer.WriteString(", \"value\":")
		// if it was a delete operation on given key, then we need to set the
		//corresponding value null. Else, we will write the response.Value
		//as-is (as the Value itself a JSON marble)
		if response.IsDelete {
			buffer.WriteString("null")
		} else {
			buffer.WriteString(string(response.Value))
		}

		buffer.WriteString(", \"timestamp\":")
		buffer.WriteString("\"")
		buffer.WriteString(time.Unix(response.Timestamp.Seconds, int64(response.Timestamp.Nanos)).String())
		buffer.WriteString("\"")

		buffer.WriteString(", \"isDelete\":")
		buffer.WriteString("\"")
		buffer.WriteString(strconv.FormatBool(response.IsDelete))
		buffer.WriteString("\"")

		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- history returning:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *CommonContract) query(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. At least 1 argument with query string should be set.")
	}
	query := args[0]

	var pageSize int32 = -1
	bookmark := ""
	if len(args) > 1 {
		value, err := strconv.ParseInt(args[1], 10, 32)
		if err == nil {
			pageSize = int32(value)
		}
	}
	if len(args) > 2 {
		bookmark = args[2]
	}

	var resultsIterator shim.StateQueryIteratorInterface
	var err error

	var metadata *sc.QueryResponseMetadata

	if pageSize > -1 {
		resultsIterator, metadata, err = APIstub.GetQueryResultWithPagination(query, pageSize, bookmark);
	} else {
		resultsIterator, err = APIstub.GetQueryResult(query)
	}

	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("{")
	buffer.WriteString("\"data\":")
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}

		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))

		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	if metadata != nil {
		buffer.WriteString(",")
		buffer.WriteString("\"meta\":")
		buffer.WriteString("{")
		buffer.WriteString("\"recordsCount\":")
		buffer.WriteString("\"")
		buffer.WriteString(fmt.Sprintf("%v", metadata.FetchedRecordsCount))
		buffer.WriteString("\"")
		buffer.WriteString(",")
		buffer.WriteString("\"bookmark\":")
		buffer.WriteString("\"")
		buffer.WriteString(metadata.Bookmark)
		buffer.WriteString("\"")
		buffer.WriteString("}")
	}

	buffer.WriteString("}")

	fmt.Printf("- query:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *CommonContract) count(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. At least 1 argument with query string should be set.")
	}
	query := args[0]

	resultsIterator, err := APIstub.GetQueryResult(query)

	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	count := 0

	for resultsIterator.HasNext() {
		_, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		count = count + 1
	}

	return shim.Success([]byte(strconv.Itoa(count)))
}

func (s *CommonContract) exists(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments. At least 1 argument with query string should be set.")
	}
	query := args[0]

	resultsIterator, err := APIstub.GetQueryResult(query)

	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	var exists string
	if resultsIterator.HasNext() {
		exists = "true"
	} else {
		exists = "false"
	}

	return shim.Success([]byte(exists))
}

func (s *CommonContract) delete(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	class := args[0]
	key := args[1]

	objectType := "type~key"
	compositeKey, err := APIstub.CreateCompositeKey(objectType, []string{class, key})
	if err != nil {
		return shim.Error(err.Error())
	}
	err = APIstub.DelState(compositeKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	return shim.Success(nil)
}

func (s *CommonContract) list(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	startKey := ""
	endKey := ""
	resultsIterator, err := APIstub.GetStateByRange(startKey, endKey)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")

		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	fmt.Printf("- list:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *CommonContract) create(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 3 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	class := args[0]
	key := args[1]

	objectType := "type~key"
	compositeKey, err := APIstub.CreateCompositeKey(objectType, []string{class, key})
	if err != nil {
		return shim.Error(err.Error())
	}

	valueAsBytes := []byte(args[2])

	err = APIstub.PutState(compositeKey, valueAsBytes)
	if err != nil {
		return shim.Error("Can not create value")
	}

	return shim.Success(valueAsBytes)
}

func (s *CommonContract) update(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 3 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	class := args[0]
	key := args[1]

	objectType := "type~key"
	compositeKey, err := APIstub.CreateCompositeKey(objectType, []string{class, key})
	if err != nil {
		return shim.Error(err.Error())
	}

	err = APIstub.DelState(compositeKey)
	if err != nil {
		return shim.Error("Can not replace the old value")
	}

	valueAsBytes := []byte(args[2])

	err = APIstub.PutState(compositeKey, valueAsBytes)
	if err != nil {
		return shim.Error("Can not update the value")
	}
	return shim.Success(valueAsBytes)
}

func (s *CommonContract) get(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {
	if len(args) < 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	class := args[0]
	key := args[1]

	objectType := "type~key"
	compositeKey, err := APIstub.CreateCompositeKey(objectType, []string{class, key})
	if err != nil {
		return shim.Error(err.Error())
	}
	valueAsBytes, err1 := APIstub.GetState(compositeKey)
	if err1 != nil {
		return shim.Error(err1.Error())
	}
	fmt.Printf("get: >%s", string(valueAsBytes))

	return shim.Success(valueAsBytes)
}

// The main function is only relevant in unit test mode. Only included here for completeness.
func main() {

	// Create a new Common Contract
	err := shim.Start(new(CommonContract))
	if err != nil {
		fmt.Printf("Error creating new Common Contract: %s", err)
	}
}
