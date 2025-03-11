package com.lchb.epcsview;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.*;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.util.BitVector;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ModbusTcpMaster {
	public static ModbusTcpMaster ModbusTcpMaster;
	public TCPMasterConnection m_Connection;
	public InetAddress m_SlaveAddress;
	public ModbusTCPTransaction m_Transaction;
	public ReadCoilsRequest m_ReadCoilsRequest=new ReadCoilsRequest();
	public ReadInputDiscretesRequest m_ReadInputDiscretesRequest = new ReadInputDiscretesRequest();
	public WriteCoilRequest m_WriteCoilRequest = new WriteCoilRequest();
	public WriteMultipleCoilsRequest m_WriteMultipleCoilsRequest = new WriteMultipleCoilsRequest();
	public ReadInputRegistersRequest m_ReadInputRegistersRequest = new ReadInputRegistersRequest();
	public ReadMultipleRegistersRequest m_ReadMultipleRegistersRequest = new ReadMultipleRegistersRequest();
	public WriteSingleRegisterRequest m_WriteSingleRegisterRequest = new WriteSingleRegisterRequest();
	public WriteMultipleRegistersRequest m_WriteMultipleRegistersRequest = new WriteMultipleRegistersRequest();
	public boolean m_Reconnecting = false;
	public int slaveunitid=0;
	public String addr="127.0.0.1";

	public String getslaveipaddr(){
		return addr;
	}

	public int getslaveunitid(){
		return slaveunitid;
	}
	
	public ModbusTcpMaster(String addr, int UnitID) {
		try {
			this.addr=addr;
			m_SlaveAddress = InetAddress.getByName(this.addr);
			m_Connection = new TCPMasterConnection(m_SlaveAddress);
			slaveunitid=UnitID;
			m_ReadCoilsRequest.setUnitID(slaveunitid);
			m_ReadInputDiscretesRequest.setUnitID(slaveunitid);
			m_WriteCoilRequest.setUnitID(slaveunitid);
			m_WriteMultipleCoilsRequest.setUnitID(slaveunitid);
			m_ReadInputRegistersRequest.setUnitID(slaveunitid);
			m_ReadMultipleRegistersRequest.setUnitID(slaveunitid);
			m_WriteSingleRegisterRequest.setUnitID(slaveunitid);
			m_WriteMultipleRegistersRequest.setUnitID(slaveunitid);
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getMessage());
		}
	}//constructor

	public boolean isConnected() {
		return m_Connection.isConnected();
	}//connect

	public void connect() throws Exception {
	    if (m_Connection != null && !m_Connection.isConnected()) {
  	      	m_Connection.connect();
  	      	m_Transaction = new ModbusTCPTransaction(m_Connection);
  	      	m_Transaction.setReconnecting(m_Reconnecting);
  	    }
	}//connect

	public void disconnect() {
	    if (m_Connection != null && m_Connection.isConnected()) {
	    	m_Connection.close();
	        m_Transaction = null;
	    }
	}//disconnect

	public void setReconnecting(boolean b) {
	    m_Reconnecting = b;
	    if (m_Transaction != null) {
	    	m_Transaction.setReconnecting(b);
	    }
	}//setReconnecting

	public boolean isReconnecting() {
		return m_Reconnecting;
	}//isReconnecting

	public synchronized BitVector readCoils(int ref, int count) throws ModbusException {
		m_ReadCoilsRequest.setReference(ref);
		m_ReadCoilsRequest.setBitCount(count);
		m_Transaction.setRequest(m_ReadCoilsRequest);
		m_Transaction.execute();
		BitVector bv = ((ReadCoilsResponse) m_Transaction.getResponse()).getCoils();
		bv.forceSize(count);
		return bv;
	}//readCoils

	public synchronized boolean writeCoil(int ref, boolean state) throws ModbusException {
		m_WriteCoilRequest.setReference(ref);
		m_WriteCoilRequest.setCoil(state);
		m_Transaction.setRequest(m_WriteCoilRequest);
		m_Transaction.execute();
		return ((WriteCoilResponse) m_Transaction.getResponse()).getCoil();
	}//writeCoil

	public synchronized void writeMultipleCoils(int ref, BitVector coils) throws ModbusException {
		m_WriteMultipleCoilsRequest.setReference(ref);
		m_WriteMultipleCoilsRequest.setCoils(coils);
		m_Transaction.setRequest(m_WriteMultipleCoilsRequest);
		m_Transaction.execute();
	}//writeMultipleCoils

	public synchronized BitVector readInputDiscretes(int ref, int count) throws ModbusException {
		m_ReadInputDiscretesRequest.setReference(ref);
		m_ReadInputDiscretesRequest.setBitCount(count);
		m_Transaction.setRequest(m_ReadInputDiscretesRequest);
		m_Transaction.execute();
		BitVector bv = ((ReadInputDiscretesResponse) m_Transaction.getResponse()).getDiscretes();
		bv.forceSize(count);
		return bv;
	}//readInputDiscretes

	public synchronized InputRegister[] readInputRegisters(int ref, int count) throws ModbusException {
		m_ReadInputRegistersRequest.setReference(ref);
		m_ReadInputRegistersRequest.setWordCount(count);
		m_Transaction.setRequest(m_ReadInputRegistersRequest);
		m_Transaction.execute();
		return ((ReadInputRegistersResponse) m_Transaction.getResponse()).getRegisters();
	}//readInputRegisters

	public synchronized Register[] readMultipleRegisters(int ref, int count) throws ModbusException {
		m_ReadMultipleRegistersRequest.setReference(ref);
		m_ReadMultipleRegistersRequest.setWordCount(count);
		m_Transaction.setRequest(m_ReadMultipleRegistersRequest);
		m_Transaction.execute();
		return ((ReadMultipleRegistersResponse) m_Transaction.getResponse()).getRegisters();
	}//readMultipleRegisters

	public synchronized void writeSingleRegister(int ref, Register register) throws ModbusException {
		m_WriteSingleRegisterRequest.setReference(ref);
		m_WriteSingleRegisterRequest.setRegister(register);
		m_Transaction.setRequest(m_WriteSingleRegisterRequest);
		m_Transaction.execute();
	}//writeSingleRegister

	public synchronized void writeMultipleRegisters(int ref, Register[] registers) throws ModbusException {
		m_WriteMultipleRegistersRequest.setReference(ref);
		m_WriteMultipleRegistersRequest.setRegisters(registers);
		m_Transaction.setRequest(m_WriteMultipleRegistersRequest);
		m_Transaction.execute();
	}//writeMultipleRegisters

}//class ModbusTCPMaster
