import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form';
import './Citizen.css'
import citizen from './citizen.png'
import { BsSearch, BsArrowLeft } from 'react-icons/bs'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import axios from 'axios';
import { BsChevronRight, BsChevronDoubleRight, BsChevronDoubleLeft, BsChevronLeft } from 'react-icons/bs'


function Citizen() {
    const role_acc = JSON.parse(localStorage.getItem("user"));
    const user = role_acc.username;
    const role = role_acc.roles[0].authority;

    const [index, setIndex] = useState(0);
    const [defaultindex, setDefaultIndex] = useState(0)
    const [showButton, setShowButton ] = useState(false);
    const [code, setCode] = useState(0)
    const [countCitizen, setCountCitizen] = useState();
    const [showDetail, setShowDetail] = useState(false);
    const [division, setDivision] = useState();
    const [province, setProvince] = useState([]);
    const [district, setDistrict] = useState([]);
    const [ward, setWard] = useState([]);
    const [hamlet, setHamlet] = useState([]);
    const [show, setShow] = useState(false);
    const [citizens, setCitizens] = useState([])
    const [page, setPage] = useState(1);
    const [numberPage, setNumberPage] = useState(0);
    const [idcitizen, setIdCitizen] = useState();
    const [name, setName] = useState();
    const [dateOfBirth, setDateOfBirth] = useState();
    const [age, setAge] = useState()
    const [bloodType, setBloodType] = useState()
    const [sex, setSex] = useState()
    const [maritalStatus, setMaritalStatus] = useState()
    const [ethnicity, setEthnicity] = useState()
    const [otherNationality, setOtherNationality] = useState()
    const [religion, setReligion] = useState()
    const [address1, setAddress1] = useState()
    const [address2, setAddress2] = useState()
    const [address3, setAddress3] = useState()
    let totalPopulation;

    const CountCitizen = async () => {
        let role_name
        if (role !== 'A1') role_name = role_acc.division.administrativeUnit.shortName + " " + role_acc.division.name;
        if (role === 'A1') {
            GetPopulationInCountry()
        } else if (role === 'A2') {
            GetPopulationInProvince(user)
            setDivision(role_name)
        } else if (role === 'A3') {
            GetPopulationInDistrict(user)
            setDivision(role_name)
        } else if (role === 'EDITOR') {
            GetPopulationInWard(user)
            setDivision(role_name)
        } else if (role === 'B2') {
            GetPopulationInHalmet(user, 1)
            setDivision(role_name)
        }

    }

    const GetPopulationInCountry = async () => {
        setIndex(1)
        setDefaultIndex(1)
        const response = await axios('http://localhost:8080/api/v1/statistics/population');
        setCountCitizen(response.data)
        setDivision("Toàn quốc")
        const response_population = await axios('http://localhost:8080/api/v1/statistics/population/province')
        setProvince(response_population.data)
        setCitizens(response_population.data)
    }

    const GetPopulationInProvince = async (code) => {
        setIndex(2)
        setDefaultIndex(2)
        const response_population = await axios('http://localhost:8080/api/v1/statistics/population/district/' + code)
        totalPopulation = 0;
        setDistrict(response_population.data)
        setCitizens(response_population.data)
        for (let i = 0; i < response_population.data.length; i++) {
            totalPopulation += response_population.data[i].population
        }
        setCountCitizen(totalPopulation)
    }

    const GetPopulationInDistrict = async (code) => {
        setIndex(3)
        setDefaultIndex(3)
        const response_population = await axios('http://localhost:8080/api/v1/statistics/population/ward/' + code)
        totalPopulation = 0;
        setWard(response_population.data)
        setCitizens(response_population.data)
        for (let i = 0; i < response_population.data.length; i++) {
            totalPopulation += response_population.data[i].population
        }
        setCountCitizen(totalPopulation)
    }

    const GetPopulationInWard = async (code) => {
        setIndex(4)
        setDefaultIndex(4)
        const response_population = await axios('http://localhost:8080/api/v1/statistics/population/hamlet/' + code)
        totalPopulation = 0;
        setHamlet(response_population.data)
        setCitizens(response_population.data)
        for (let i = 0; i < response_population.data.length; i++) {
            totalPopulation += response_population.data[i].population
        }
        setCountCitizen(totalPopulation)
    }

    const GetPopulationInHalmet = async (code, page) => {
        setIndex(5)
        setDefaultIndex(5)
        console.log(role_acc)
        const response_population = await axios('http://localhost:8080/api/v1/citizen/hamlet/' + code + '?page=' + page)
        setHamlet(response_population.data.citizens)
        setCitizens(response_population.data.citizens)
        setNumberPage(response_population.data.totalPages)
        setCountCitizen(response_population.data.totalElements)
    }

    const BackToClickData = async () => {
        setCode(code.substring(0, code.length - 2))
        if (user !== 'tw' && (code.length === user.length + 2)) {
            setShowButton(false)
        } else if (user === 'tw' && code.length === 2) {
            setShowButton(false)
        }
        if (code.length - 2 === 6) {
            GetPopulationInWard(code.substring(0, code.length - 2))
            const response = await axios('http://localhost:8080/api/v1/ward/' + code.substring(0, code.length - 2))
            setDivision(response.data.name)
        }
        else if (code.length - 2 === 4) {
            GetPopulationInDistrict(code.substring(0, code.length - 2))
            const response = await axios('http://localhost:8080/api/v1/district/' + code.substring(0, code.length - 2))
            setDivision(response.data.name)
        }
        else if (code.length - 2=== 2) {
            GetPopulationInProvince(code.substring(0, code.length - 2))
            const response = await axios('http://localhost:8080/api/v1/province/' + code.substring(0, code.length - 2))
            setDivision(response.data.name)
        }
        else if (code.length - 2 === 0) GetPopulationInCountry()
    }

    const Pagination = () => {
        return (
            <div className="panigation">
                {(page !== 1) ? <div className="page" onClick={() => {
                    setPage(1)
                    GetPopulationInHalmet(user, 1)
                }}><BsChevronDoubleLeft /></div> : null}
                {(page !== 1) ? <div className="page" onClick={() => {
                    setPage(page - 1)
                    GetPopulationInHalmet(user, page - 1)
                }}><BsChevronLeft /></div> : null}
                <div className="pageNumber">{page}</div>
                {(page !== numberPage) ? <div className="page" onClick={() => {
                    setPage(page + 1)
                    GetPopulationInHalmet(user, page + 1)
                }}><BsChevronRight /></div> : null}
                {(page !== numberPage) ? <div className="page" onClick={() => {
                    setPage(numberPage)
                    GetPopulationInHalmet(user, numberPage)
                }}><BsChevronDoubleRight /></div> : null}
            </div>
        )
    }


    useEffect(() => {
        CountCitizen();
    }, [])

    const listCitizens = citizens.map((post, index) =>
        <tr key={index} onClick={() => { GetDataOnClick(post.code, post.name) }}>
            <td>{post.code}</td>
            <td>{post.name}</td>
            <td>{post.population}</td>
        </tr>
    );

    const listCitizensInHalemt = citizens.map((post, index) =>
        <tr key={index} onClick={() => { fetchDetailCitizen(post.nationalId) }}>
            <td>{post.nationalId}</td>
            <td>{post.name}</td>
            <td>{post.dateOfBirth}</td>
            <td>{post.sex}</td>
        </tr>
    )

    const fetchDetailCitizen = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/citizen/' + code);
            setIdCitizen(response.data.nationalId)
            setBloodType(response.data.bloodType)
            setDateOfBirth(response.data.dateOfBirth)
            if (response.data.religion !== null) setReligion(response.data.religion.name)
            else setReligion(null)
            setName(response.data.name)
            setOtherNationality(response.data.otherNationality)
            setEthnicity(response.data.ethnicity.name)
            setMaritalStatus(response.data.maritalStatus)
            setSex(response.data.sex)
            setAddress1(response.data.addresses[0].hamlet.administrativeUnit.fullName + " " + response.data.addresses[0].hamlet.name + ", " + response.data.addresses[0].hamlet.ward.name + ", " + response.data.addresses[0].hamlet.ward.district.name + ", " + response.data.addresses[0].hamlet.ward.district.province.name)
            setAddress2(response.data.addresses[1].hamlet.administrativeUnit.fullName + " " + response.data.addresses[1].hamlet.name + ", " + response.data.addresses[1].hamlet.ward.name + ", " + response.data.addresses[1].hamlet.ward.district.name + ", " + response.data.addresses[1].hamlet.ward.district.province.name)
            if (response.data.addresses.length === 3) setAddress3(response.data.addresses[2].hamlet.administrativeUnit.fullName + " " + response.data.addresses[2].hamlet.name + ", " + response.data.addresses[2].hamlet.ward.name + ", " + response.data.addresses[2].hamlet.ward.district.name + ", " + response.data.addresses[2].hamlet.ward.district.province.name)
            else setAddress3(null)
            setShowDetail(true);
            console.log(response.data.addresses)
            setShowButton(false)
        } catch (err) {
            console.error(err);
        }
    };

    const GetDataOnClick = (code, name) => {
        setCode(code)
        setDivision(name)
        if (index === 1) GetPopulationInProvince(code);
        else if (index === 2) GetPopulationInDistrict(code);
        else if (index === 3) GetPopulationInWard(code);
        else if (index === 4) GetPopulationInHalmet(code, 1);
        setShowButton(true)
    }

    const BackToDetailInformation = () => {
        setShowDetail(false)
        if (code.length !== user.length) {
            setShowButton(true)
        }
    }

    const DetailInformation = () => {
        return (
            <div className="detailed-resident-info">
                <div id="detailed-resident-info-header">
                    <Button className="returnButton" onClick={() => {BackToDetailInformation()}}><BsArrowLeft /></Button>
                    <h2 id="detailed-resident-info-title">Thông tin chi tiết</h2>
                </div>
                <Table bordered hover className="tableInfo">
                    <tbody>
                        <tr>
                            <th className="row-head">Họ và tên</th>
                            <th className="row-data">{name}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Số CMND/CCCD</th>
                            <th className="row-data">{idcitizen}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Ngày tháng năm sinh</th>
                            <th className="row-data">{dateOfBirth}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Giới tính</th>
                            <th className="row-data">{sex}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Quê quán</th>
                            <th className="row-data">{address1}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Địa chỉ thường trú</th>
                            <th className="row-data">{address2}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Địa chỉ tạm trú</th>
                            <th className="row-data">{(address3 === null) ? "Không có" : address3}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Nhóm máu</th>
                            <th className="row-data">{bloodType}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Tình trạng hôn nhân</th>
                            <th className="row-data">{maritalStatus}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Dân tộc</th>
                            <th className="row-data">{ethnicity}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Tôn giáo</th>
                            <th className="row-data">{(religion === null) ? "Không có" : religion}</th>
                        </tr>
                        <tr>
                            <th className="row-head">Quốc tịch khác</th>
                            <th className="row-data">{(otherNationality === "") ? "Không có" : otherNationality}</th>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }

    const TableResidential = () => {
        return (
            <div>
                <div>
                    <Table striped bordered hover size="sm" className="tableResidential">
                        <thead>
                            <tr>
                                <th>Mã đơn vị</th>
                                <th>Tên đơn vị</th>
                                <th>Tổng dân số</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listCitizens}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }

    const TableResidentialInHalmet = () => {
        return (
            <div>
                <div>
                    <Table striped bordered hover size="sm" className="tableResidentialInHamlet">
                        <thead>
                            <tr>
                                <th>CMMD/CCCD</th>
                                <th>Họ và tên</th>
                                <th>Ngày sinh</th>
                                <th>Giới tính</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listCitizensInHalemt}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }

    return (
        <div>
            <NavbarPage />
            <div className="d-flex justify-content-between">
                <div className="flex_citizen_first">
                    <div className="titleCitizen">TỔNG DÂN SỐ</div>
                    <div className="titleCitizen">Khu vực: {division}</div>
                    <div className="countCitizen">{countCitizen}</div>
                    <img src={citizen} className='logoCitizen' />
                </div>
                <div className="flex_citizen_second">
                    <div id="detailed-resident-info-header">
                        {(showButton) ? <Button className="returnShow" onClick={() => {BackToClickData()}}><BsArrowLeft /> Trở về</Button> : null}
                    </div>
                    {((index === 5) && (!showDetail)) ? <TableResidentialInHalmet /> : ((index === 5) && (showDetail) ? <DetailInformation /> : null)}
                    {(index !== 5) ? <TableResidential /> : null}
                </div>
                {(index === 5 && (!showDetail)) ? <Pagination /> : null}
            </div>
        </div>
    );
}

export default Citizen;
