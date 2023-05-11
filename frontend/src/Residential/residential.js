import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import Form from 'react-bootstrap/Form';
import './residential.css'
import { AiOutlineClose } from 'react-icons/ai'
import { BsSearch, BsArrowLeft } from 'react-icons/bs'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import axios from 'axios';


function Residential() {
    const [showOption, setShowOption] = useState(true);
    const [showDetail, setShowDetail] = useState(false);
    const [regionId, setRegionID] = useState();
    const [provinceCode, setProvinceCode] = useState();
    const [districtCode, setDistrictCode] = useState();
    const [wardCode, setWardCode] = useState();
    const [hamletCode, setHamletCode] = useState();
    const [regions, setRegions] = useState([]);
    const [provinces, setProvinces] = useState([]);
    const [districts, setDistricts] = useState([]);
    const [wards, setWards] = useState([]);
    const [hamlets, setHalmets] = useState([]);
    const [citizen, setCitizen] = useState([]);
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

    const fetchCitizen = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/citizen/');
            setCitizen(response.data);

        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetailCitizen = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/citizen/' + code);
            setIdCitizen(response.data.id)
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
            if (response.data.addresses.length === 3) setAddress3(response.data.addresses[2].hamlet.administrativeUnit.fullName + " " + response.data.addresses[2].hamlet.name + ", " + response.data.addresses[1].hamlet.ward.name + ", " + response.data.addresses[2].hamlet.ward.district.name + ", " + response.data.addresses[2].hamlet.ward.district.province.name)
            else setAddress3(null)
            setShowDetail(true);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchRegion = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/administrativeRegion/');
            setRegions(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchProvince = async (provinceCode) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/administrativeRegionId/' + provinceCode);
            setProvinces(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchFullProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/');
            setProvinces(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchWard = async (districtCode) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/districtCode/' + districtCode);
            setWards(response.data);
        } catch (err) {
            console.error(err);
        }
    };


    const fetchDistrict = async (provinceCode) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/provinceCode/' + provinceCode);
            setDistricts(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchHalmet = async (districtCode) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/hamlet/wardCode/' + districtCode);
            setHalmets(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchRegion();
        fetchCitizen();
        fetchFullProvince();
    }, [])

    const regionItems = regions.map((region) =>
        <option value={region.id} key={region.id}>{region.id + ". " + region.name}</option>
    );

    const listProvinces = provinces.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistricts = districts.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWards = wards.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listHamlets = hamlets.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listCitizen = citizen.map((post) =>
        <tr key={post.id} value={post.id} onClick={() => { fetchDetailCitizen(post.id) }}>
            <td>{post.id}</td>
            <td>{post.name}</td>
            <td>{post.dateOfBirth}</td>
            <td>{post.sex}</td>
            {post.addresses.map((address) =>
                <td>{address.hamlet.administrativeUnit.fullName + " " + address.hamlet.name + ", " + address.hamlet.ward.name + ", " + address.hamlet.ward.district.name + ", " + address.hamlet.ward.district.province.name}</td>
            )}
        </tr>

    );

    const DetailInformation = () => {
        return (
            <div className="detailed-resident-info">
                <div id="detailed-resident-info-header">
                    <Button className="returnButton" onClick={() => {setShowDetail(false)}}><BsArrowLeft/></Button>
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
                            <th className="row-head">Nơi sinh</th>
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
                            <th className="row-data">{(otherNationality === null) ? "Không có" : otherNationality}</th>
                        </tr>
                    </tbody>
                </Table>
            </div>
        );
    }

    const SelectShowOption = (() => {
        return (
            <div>
                <Button className='buttonIcon' onClick={() => setShowOption(false)}><AiOutlineClose /></Button>
                <Button className='buttonIcon'><BsSearch /></Button>
                <div className='selectOption'>
                    <Form.Select aria-label="Default select example" value={hamletCode} onChange={(e) => {
                        setHamletCode(e.target.value)
                    }}>
                        <option>Thôn/Tổ dân phố</option>
                        {listHamlets}
                    </Form.Select>
                </div>
                <div className="selectOption">
                    <Form.Select aria-label="Default select example" value={wardCode} onChange={(e) => {
                        if (e.target.value !== '0') fetchHalmet(e.target.value)
                        else setHalmets([])
                        setWardCode(e.target.value)
                    }}>
                        <option value='0'>Xã/phường/thị trấn</option>
                        {listWards}
                    </Form.Select>
                </div>
                <div className="selectOption">
                    <Form.Select aria-label="Default select example" value={districtCode} onChange={(e) => {
                        if (e.target.value !== '0') fetchWard(e.target.value)
                        else setWards([])
                        setDistrictCode(e.target.value)
                        setHalmets([])
                    }}>
                        <option value='0'>Quận/huyện/thị xã</option>
                        {listDistricts}
                    </Form.Select>
                </div>
                <div className="selectOption">
                    <Form.Select aria-label="Default select example" value={provinceCode} onChange={(e) => {
                        if (e.target.value !== '0') fetchDistrict(e.target.value)
                        else setDistricts([]);
                        setProvinceCode(e.target.value)
                        setHalmets([])
                        setWards([])
                    }}>
                        <option value="0">Tỉnh/Thành phố</option>
                        {listProvinces}
                    </Form.Select>
                </div>
                <div className="selectOption">
                    <Form.Select aria-label="Default select example" value={regionId} onChange={(e) => {
                        if (e.target.value !== '0') fetchProvince(e.target.value)
                        else {
                            fetchFullProvince();
                            setProvinceCode(0);
                        }
                        setRegionID(e.target.value)
                        setHalmets([])
                        setDistricts([])
                        setWards([])
                    }}>
                        <option value='0'>Khu vực</option>
                        {regionItems}
                    </Form.Select>
                </div>
            </div>
        )
    })

    const TableResidential = () => {
        return (
            <div>
                {(!showOption) ? <Button className='buttonIcon' onClick={() => setShowOption(true)}>Lọc theo địa chỉ thường trú</Button> : null}
                {(showOption) ? <SelectShowOption /> : null}
                <div>
                    <Table striped bordered hover size="sm" className='tableCitizen'>
                        <thead>
                            <tr>
                                <th>CMMD/CCCD</th>
                                <th>Họ và tên</th>
                                <th>Ngày sinh</th>
                                <th>Giới tính</th>
                                <th>Quê quán</th>
                                <th>Địa chỉ thường trú</th>
                                <th>Địa chỉ tạm trú</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listCitizen}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <div>
                {(!showDetail) ? <TableResidential /> : <DetailInformation />}
            </div>
        </div>
    );
}

export default Residential;
