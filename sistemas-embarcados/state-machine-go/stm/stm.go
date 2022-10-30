package stm

type states_t int

//  /* possiveis estados da maquina de estados de comunicacao */
const (
	ST_STX  states_t = iota // EnumIndex = 0
	ST_ADDR                 // EnumIndex = 1
	ST_QTD                  // EnumIndex = 2
	ST_DATA                 // EnumIndex = 3
	ST_CHK                  // EnumIndex = 4
	ST_ETX                  // EnumIndex = 5
	ST_END                  // EnumIndex = 6

)

/* constantes usadas na comunicacao */
const (
	STX = "$"
	ETX = "\n"
)

/* numero maximo de bytes do buffer de dados */
const (
	MAX_BUFFER = 128
)

type handle_t func(*rune)

//  //Funções executadas nos estados
type action_t func(*StateMachine, *rune)

type StateMachine struct {
	state         states_t
	buffer        [MAX_BUFFER]rune
	chk           rune
	qtd           rune
	idx           int
	flag_addr     rune
	addr          uint8
	my_addr       uint8
	action        [ST_END]action_t
	HandlePackage handle_t
	HandleError   handle_t
}

// ------------------------------------------------------------------------------------

func stSTX(sm *StateMachine, data *rune) {
	if string(*data) == STX {
		sm.idx = 0
		sm.qtd = 0
		sm.chk = 0
		sm.state = ST_ADDR
		sm.flag_addr = 0
		//sm.result = false;
	}
}

func stAddr(sm *StateMachine, data *rune) {
	if !sm.flag_addr {
		sm.addr = uint(data)
		sm.chk = data
		sm.flag_addr = 1
	} else {
		sm.flag_addr = 0
		sm.addr |= (uint)(data << 8)
		if sm.addr == sm.my_addr {
			sm.state = ST_QTD
			sm.chk += data
		} else {
			sm.state = ST_STX
			if sm.HandleError != NULL {
				sm.HandleError("Endereço errado...\n\r")
			}
		}
	}
}

func stQtd(sm *StateMachine, data *rune) {
	if (data >= 1) && (data <= 128) {
		sm.qtd = data
		sm.chk += data
		sm.state = ST_DATA
	} else {
		sm.state = ST_STX
	}
}

func stData(sm *StateMachine, data *rune) {
	// sm.buffer[(sm.idx+=1)] = data;
	sm.chk += data
	// if (--sm.qtd == 0) {
	//     sm.state = ST_CHK;
	// }
}

func stChk(sm *StateMachine, data *rune) {
	if data == sm.chk {
		//sm.result = true;
		sm.state = ST_ETX
	} else {
		//sm.result = false;
		sm.state = ST_STX
		if sm.HandleError != NULL {
			sm.HandleError("Falha de Checksum...\n\r")
		}
	}
}

func stETX(sm *StateMachine, data *rune) {
	if data == ETX {
		//if (sm.result == true){
		sm.buffer[sm.idx] = "\\0"
		if sm.HandlePackage != NULL {
			sm.HandlePackage(sm.buffer)
		}
		//}
	}
	sm.state = ST_STX
}

func InitSM(sm *StateMachine, my_addr uint8, handle_function handle_t, error_function handle_t) {
	sm.state = ST_STX
	sm.my_addr = my_addr
	sm.buffer[0] = 0
	sm.chk = 0
	sm.idx = 0
	sm.qtd = 0
	sm.action[ST_STX] = stSTX
	sm.action[ST_ADDR] = stAddr
	sm.action[ST_QTD] = stQtd
	sm.action[ST_DATA] = stData
	sm.action[ST_CHK] = stChk
	sm.action[ST_ETX] = stETX
	sm.HandlePackage = handle_function
	sm.HandleError = error_function
}

func ExecSM(sm *StateMachine, data *rune, qtd int) {
	for i := 0; i < qtd; i++ {
		// sm.action[sm.state](sm, data[i])
	}

}
