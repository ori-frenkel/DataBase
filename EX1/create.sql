CREATE TABLE Person(
  ID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  NAME VARCHAR(20) NOT NULL
  );
  
CREATE TABLE BIRTHDAY(
  BDAY INTEGER,
  BMONTH INTEGER,
  BYEAR INTEGER,
  PRIMARY KEY (BDAY, BMONTH, BYEAR),
  CHECK(BDAY>0 AND BMONTH>0 AND BDAY<32 AND BMONTH<32)
  );
  
CREATE TABLE Customer(
  ID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  PhoneNumber INTEGER NOT NULL,
  FOREIGN KEY (ID) REFERENCES Person(ID)
  ); 
  
CREATE TABLE HasBday(
  CustomerID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  Bday INTEGER NOT NULL,
  Bmonth INTEGER NOT NULL,
  Byear INTEGER NOT NULL,
  FOREIGN KEY (CustomerID) REFERENCES Customer(ID)
  );
  
CREATE TABLE Pilot(
  ID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  FOREIGN KEY (ID) REFERENCES Person(ID)
  );
  
CREATE TABLE AirPlane(
  ID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  ManufactureYear INTEGER NOT NULL,
  Model VARCHAR(20) NOT NULL
  );

CREATE TABLE Flight(
  FlightID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  FlightDate INTEGER NOT NULL,
  FlightTime INTEGER NOT NULL,
  AirPlaneID INTEGER NOT NULL,
  FOREIGN KEY (AirPlaneID) REFERENCES AirPlane(ID)
  );

CREATE TABLE Seat(
  SeatRow INTEGER NOT NULL,
  SeatAisle INTEGER NOT NULL,
  UNIQUE (SeatRow, SeatAisle),
  PRIMARY KEY(SeatRow, SeatAisle)
  );
  
Create TABLE NormalSeat(
  NormalRow INTEGER NOT NULL,
  NormalAisle INTEGER NOT NULL,
  FOREIGN KEY (NormalRow, NormalAisle) REFERENCES Seat(SeatRow, SeatAisle),
  UNIQUE(NormalRow, NormalAisle),
  PRIMARY KEY(NormalRow, NormalAisle)
  );

Create TABLE VIPSeat(
  VipRow INTEGER NOT NULL,
  VIPAisle INTEGER NOT NULL,
  FOREIGN KEY (VipRow, VIPAisle) REFERENCES Seat(SeatRow, SeatAisle),
  UNIQUE(VipRow, VIPAisle),
  PRIMARY KEY(VipRow, VIPAisle)
  );
  
CREATE TABLE NormalCustomer(
  ID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  PhoneNumber INTEGER NOT NULL,
  NormalSeatRow INTEGER NOT NULL,
  NormalSeatAisle INTEGER NOT NULL,
  FOREIGN KEY (NormalSeatRow, NormalSeatAisle) REFERENCES NormalSeat(NormalRow, NormalAisle),
  UNIQUE(NormalSeatRow, NormalSeatAisle),
  FOREIGN KEY (ID) REFERENCES Customer(ID)
  );
  
CREATE TABLE VIPCustomer(
  ID INTEGER UNIQUE NOT NULL PRIMARY KEY,
  Points INTEGER NOT NULL,
  PhoneNumber INTEGER NOT NULL,
  SeatRow INTEGER NOT NULL,
  SeatAisle INTEGER NOT NULL,
  FOREIGN KEY (SeatRow, SeatAisle) REFERENCES Seat(SeatRow, SeatAisle),
  UNIQUE(SeatRow, SeatAisle),
  FOREIGN KEY (ID) REFERENCES Customer(ID),
  CHECK (VIPCustomer.points>=0)
  );
  
CREATE TABLE FlightInvitation(
  CustomerID INTEGER NOT NULL,
  Price INTEGER NOT NULL,
  FlightID INTEGER UNIQUE NOT NULL,
  PRIMARY KEY(CustomerID, FlightID),
  FOREIGN KEY (CustomerID) REFERENCES Customer(ID),
  FOREIGN KEY (FlightID) REFERENCES Flight(FlightID),
  UNIQUE(CustomerID, FlightID),
  CHECK (FlightInvitation.Price>=0)
  );
  
CREATE TABLE HasPilots(
  PilotID INTEGER NOT NULL,
  FlightID INTEGER NOT NULL,
  FOREIGN KEY (PilotID) REFERENCES Pilot(ID),
  FOREIGN KEY (FlightID) REFERENCES Flight(FlightID),
  PRIMARY KEY (PilotID, FlightID)
  );
