import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../../../Navbar/NavbarPage.js';
import { Form, Button } from 'react-bootstrap';
import PDFFile from '../file/mau_phieu.pdf'
import '../css/FindCitizen.css'
import ExcelFile from '../file/mau_excel.xlsx'
import { useState, useEffect } from 'react';
import Modal from 'react-bootstrap/Modal';
import Table from 'react-bootstrap/Table';
import axios from 'axios';
import { BsArrowLeft } from 'react-icons/bs'
import { AiFillCaretRight, AiFillCaretDown } from 'react-icons/ai'

function FindCititzen() {
    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.username;
    const config = {
        headers: {
            Authorization: `Bearer ${user_account.accessToken}`
        },
    };

    const [page, setPage] = useState(1);
    const [status, setStatus] = useState(user_account.declarationStatus);
    const [showNotify, setShowNotify] = useState(false);
    const [showInput, setShowInput] = useState(false);
    const [checked, setChecked] = useState(false);
    const [provinceCodeAddress1, setProvinceCodeAddress1] = useState("");
    const [districtCodeAddress1, setDistrictCodeAddress1] = useState("");
    const [wardCodeAddress1, setWardCodeAddress1] = useState("");
    const [hamletCodeAddress1, setHamletCodeAddress1] = useState("");
    const [provinceCodeAddress2, setProvinceCodeAddress2] = useState(user.substring(0, 2));
    const [districtCodeAddress2, setDistrictCodeAddress2] = useState(user.substring(0, 4));
    const [wardCodeAddress2, setWardCodeAddress2] = useState(user.substring(0, 6));
    const [hamletCodeAddress2, setHamletCodeAddress2] = useState(user);
    const [provinceCodeAddress3, setProvinceCodeAddress3] = useState("");
    const [districtCodeAddress3, setDistrictCodeAddress3] = useState("");
    const [wardCodeAddress3, setWardCodeAddress3] = useState("");
    const [hamletCodeAddress3, setHamletCodeAddress3] = useState("");
    const [provincesAddress1, setProvincesAddress1] = useState([]);
    const [provincesAddress2, setProvincesAddress2] = useState([]);
    const [provincesAddress3, setProvincesAddress3] = useState([]);
    const [districtsAddress1, setDistrictsAddress1] = useState([]);
    const [districtsAddress2, setDistrictsAddress2] = useState([]);
    const [districtsAddress3, setDistrictsAddress3] = useState([]);
    const [wardsAddress1, setWardsAddress1] = useState([]);
    const [wardsAddress2, setWardsAddress2] = useState([]);
    const [wardsAddress3, setWardsAddress3] = useState([]);
    const [hamletsAddress1, setHamletsAddress1] = useState([]);
    const [hamletsAddress2, setHamletsAddress2] = useState([]);
    const [hamletsAddress3, setHamletsAddress3] = useState([]);
    const [ethnicitys, setEthnicitys] = useState([]);
    const [religions, setReligions] = useState([]);
    const [bloodType, setBloodType] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [ethnicity, setEthnicity] = useState("");
    const [sex, setSex] = useState("");
    const [marriageStatus, setMarriageStatus] = useState("");
    const [religionId, setReligionId] = useState("");
    const [religionName, setReligionName] = useState("");
    const [fullName, setFullName] = useState("");
    const [fullNameAssociation1, setFullNameAssociation1] = useState("")
    const [fullNameAssociation2, setFullNameAssociation2] = useState("")
    const [fullNameAssociation3, setFullNameAssociation3] = useState("")
    const [fullNameAssociation4, setFullNameAssociation4] = useState("");
    const [idAssociation1, setIdAssociation1] = useState(null)
    const [idAssociation2, setIdAssociation2] = useState(null)
    const [idAssociation3, setIdAssociation3] = useState(null)
    const [idAssociation4, setIdAssociation4] = useState(null);
    const [nationalId, setNationalId] = useState("");
    const [nationalIdAssociation1, setNationalIdAssociation1] = useState("");
    const [nationalIdAssociation2, setNationalIdAssociation2] = useState("");
    const [nationalIdAssociation3, setNationalIdAssociation3] = useState("");
    const [nationalIdAssociation4, setNationalIdAssociation4] = useState("");
    const [otherNationality, setOtherNationality] = useState("");
    const [nameProvinceAddress2, setNameProvinceAddress2] = useState("")
    const [nameDistrictAddress2, setNameDistrictAddress2] = useState("")
    const [nameWardAddress2, setNameWardAddress2] = useState("")
    const [nameHamletAddress2, setNameHamletAddress2] = useState("")
    const [associationList, setAssociationList] = useState([])
    const [declaration, setDeclaration] = useState()
    const [file, setFile] = useState()
    const [job, setJob] = useState("")
    const [educationalLevel, setEducationalLevel] = useState("")
    const [message, setMessage] = useState("")
    const [firstSelectOption, setFirstSelectOption] = useState(false)
    const [secondSelectOption, setSecondSelectOption] = useState(false)
    const [heightScreen, setHeightScreen] = useState(window.screen.height)
    const [idcitizen, setIdCitizen] = useState();
    const [name, setName] = useState();
    const [maritalStatus, setMaritalStatus] = useState('')
    const [listCitizens, setListCitizens] = useState([])
    const [showTableDetail, setShowTableDetail] = useState(false)
    const [showEditCitizen, setShowEditCitizen] = useState(false)
    const [showOption, setShowOption] = useState(false)


    const GetDecleration = async () => {
        try {
            const response = await axios("http://localhost:8080/api/v1/user/" + user, config)
            setDeclaration(response.data.declaration)
        } catch (err) {
            console.error(err);
        }
    }
    // get list of provinces
    const fetchProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/', config);
            setProvincesAddress1(response.data);
            setProvincesAddress2(response.data)
            setProvincesAddress3(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    // get list of districts in province
    const fetchDistrict = async (code, index) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/by-province/' + code, config);
            if (index === 1) setDistrictsAddress1(response.data);
            else if (index === 2) setDistrictsAddress2(response.data);
            else if (index === 3) setDistrictsAddress3(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    // get list of wards in district
    const fetchWard = async (code, index) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/ward/by-district/' + code, config);
            if (index === 1) setWardsAddress1(response.data);
            else if (index === 2) setWardsAddress2(response.data);
            else if (index === 3) setWardsAddress3(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    // get list of hamlets in ward
    const fetchHamlet = async (code, index) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + code, config);
            if (index === 1) setHamletsAddress1(response.data);
            else if (index === 2) setHamletsAddress2(response.data);
            else if (index === 3) setHamletsAddress3(response.data);
            console.log(response.data)
        } catch (err) {
            console.error(err);
        }
    };

    // get list of ethnicities
    const fetchEthnicity = async () => {
        try {
            const response = await axios(' http://localhost:8080/api/v1/ethnicity/', config);
            setEthnicitys(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    // get list of regions
    const fetchRegion = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/religion/', config);
            setReligions(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const UpdateDataCitizen = async () => {
        const updateCitizen = {
            "nationalId": nationalId,
            "name": name,
            "dateOfBirth": dateOfBirth,
            "bloodType": bloodType,
            "sex": sex,
            "maritalStatus": maritalStatus,
            "educationalLevel": educationalLevel,
            "job": job,
            "ethnicityId": Number(ethnicity),
            "otherNationality": otherNationality,
            "religion": (religionId === 0) ? null : Number(religionId),
            "addresses": [
                {
                    "id": null,
                    "addressType": 1,
                    "hamletCode": hamletCodeAddress1,
                    "provinceCode": hamletCodeAddress1.substring(0, 2)

                },
                {
                    "id": null,
                    "addressType": 2,
                    "hamletCode": hamletCodeAddress2,
                    "provinceCode": hamletCodeAddress2.substring(0, 2)
                }
            ],
            "associations": []
        }

        if (hamletCodeAddress3 !== '') {
            updateCitizen.addresses.push({
                "id": null,
                "addressType": 3,
                "hamletCode": hamletCodeAddress3,
                "provinceCode": hamletCodeAddress3.substring(0, 2)
            })
        }

        const associationsUpdate = [
            {
                "id": idAssociation1,
                "associatedCitizenNationalId": nationalIdAssociation1,
                "associatedCitizenName": fullNameAssociation1,
                "associationType": {
                    "id": 1,
                    "name": "Cha-con"
                }
            },
            {
                "id": idAssociation2,
                "associatedCitizenNationalId": nationalIdAssociation2,
                "associatedCitizenName": fullNameAssociation2,
                "associationType": {
                    "id": 2,
                    "name": "Mẹ-con"
                }
            },
            {
                "id": idAssociation3,
                "associatedCitizenNationalId": nationalIdAssociation3,
                "associatedCitizenName": fullNameAssociation3,
                "associationType": {
                    "id": 3,
                    "name": "Chồng-vợ"
                }
            },
            {
                "id": idAssociation4,
                "associatedCitizenNationalId": nationalIdAssociation4,
                "associatedCitizenName": fullNameAssociation4,
                "associationType": {
                    "id": 4,
                    "name": "Người đại diện hợp pháp"
                }
            }
        ]

        for (let i = 0; i < associationList.length; i++) {
            for (let j = 0; j < associationsUpdate.length; j++) {
                if (associationList[i].id === associationsUpdate[j].id) {
                    updateCitizen.associations.push(associationsUpdate[j])
                }
            }
        }

        for (let i = 0; i < associationsUpdate.length; i++) {
            if (associationsUpdate[i].id === null && associationsUpdate[i].associatedCitizenNationalId !== '') {
                updateCitizen.associations.push(associationsUpdate[i])
            }
        }

        try {
            await axios.put("http://localhost:8080/api/v1/citizen/save/" + idcitizen, updateCitizen, config)
            setShowEditCitizen(false)
        } catch (error) {
            console.log(error)
        }
        console.log(updateCitizen)
    }

    const FindCitizenByMultiInfo = async () => {
        const updateCitizen = {
            "nationalId": (nationalId === '') ? null : nationalId,
            "name": (name === '') ? null : name,
            "dateOfBirth": (dateOfBirth === '') ? null : dateOfBirth,
            "bloodType": (bloodType === '') ? null : bloodType,
            "sex": (sex === '') ? null : sex,
            "maritalStatus": (maritalStatus === '') ? null : maritalStatus,
            "educationalLevel": (educationalLevel === '') ? null : educationalLevel,
            "job": (job === '') ? null : job,
            "ethnicityId": (ethnicity === '') ? null : Number(ethnicity),
            "otherNationality": (otherNationality === '') ? null : otherNationality ,
            "religion": (religionId === "") ? null : Number(religionId),
            "addresses": [],
            "associations": []
        }

        if (hamletCodeAddress1 !== '') {
            updateCitizen.addresses.push({
                "id": null,
                "addressType": 1,
                "hamletCode": hamletCodeAddress1,
                "provinceCode": hamletCodeAddress1.substring(0, 2)
            })
        }

        if (hamletCodeAddress2 !== '') {
            updateCitizen.addresses.push({
                "id": null,
                "addressType": 2,
                "hamletCode": hamletCodeAddress2,
                "provinceCode": hamletCodeAddress2.substring(0, 2)
            })
        }

        if (hamletCodeAddress3 !== '') {
            updateCitizen.addresses.push({
                "id": null,
                "addressType": 3,
                "hamletCode": hamletCodeAddress3,
                "provinceCode": hamletCodeAddress3.substring(0, 2)
            })
        }

        if (nationalIdAssociation1 !== '' || fullNameAssociation1 !== '') {
            updateCitizen.associations.push({
                "id": null,
                "associatedCitizenNationalId": (nationalIdAssociation1 === '') ? null : nationalIdAssociation1,
                "associatedCitizenName": (fullNameAssociation1 === '') ? null : fullNameAssociation1,
                "associationType": {
                    "id": 1,
                    "name": "Cha-con"
                }
            })
        }

        if (nationalIdAssociation2 !== '' || fullNameAssociation2 !== '') {
            updateCitizen.associations.push({
                "id": null,
                "associatedCitizenNationalId": (nationalIdAssociation2 === '') ? null : nationalIdAssociation2,
                "associatedCitizenName": (fullNameAssociation2 === '') ? null : fullNameAssociation2,
                "associationType": {
                    "id": 2,
                    "name": "Mẹ-con"
                }
            })
        }

        
        if (nationalIdAssociation3 !== '' || fullNameAssociation3 !== '') {
            updateCitizen.associations.push({
                "id": null,
                "associatedCitizenNationalId": (nationalIdAssociation3 === '') ? null : nationalIdAssociation3,
                "associatedCitizenName": (fullNameAssociation3 === '') ? null : fullNameAssociation3,
                "associationType": {
                    "id": 3,
                    "name": "Chồng-vợ"
                }
            })
        }

        if (nationalIdAssociation4 !== '' || fullNameAssociation4 !== '') {
            updateCitizen.associations.push({
                "id": null,
                "associatedCitizenNationalId": (nationalIdAssociation4 === '') ? null : nationalIdAssociation4,
                "associatedCitizenName": (fullNameAssociation4 === '') ? null : fullNameAssociation4,
                "associationType": {
                    "id": 4,
                    "name": "Người đại diện hợp pháp"
                }
            })
        }
        console.log(updateCitizen)
        try {
            const response = await axios.post("http://localhost:8080/api/v1/citizen/search", updateCitizen, config)
            console.log(response.data)
            setListCitizens(response.data)
            setShowTableDetail(true)
            setShowOption(false)
        } catch (error) {
            console.log(error.response)
        }
    }

    const GetDataCitizen = async (nationalId) => {
        const response = await axios.get("http://localhost:8080/api/v1/citizen/" + nationalId, config)
        setName(response.data.name)
        setIdCitizen(response.data.nationalId)
        setNationalId(response.data.nationalId)
        setDateOfBirth(response.data.dateOfBirth)
        setSex(response.data.sex)
        setBloodType(response.data.bloodType)
        setMaritalStatus(response.data.maritalStatus)
        setEthnicity(response.data.ethnicity.id)
        setReligionId(response.data.religion.id)
        setOtherNationality(response.data.otherNationality)
        setJob(response.data.job)
        setEducationalLevel(response.data.educationalLevel)
        setProvinceCodeAddress1(response.data.addresses[0].hamlet.ward.district.province.code)
        setProvinceCodeAddress2(response.data.addresses[1].hamlet.ward.district.province.code)
        if (response.data.addresses.length === 3) setProvinceCodeAddress3(response.data.addresses[2].hamlet.ward.district.province.code)
        setDistrictCodeAddress1(response.data.addresses[0].hamlet.ward.district.code)
        fetchDistrict(response.data.addresses[0].hamlet.ward.district.province.code, 1)
        setDistrictCodeAddress2(response.data.addresses[1].hamlet.ward.district.code)
        fetchDistrict(response.data.addresses[1].hamlet.ward.district.province.code, 2)
        if (response.data.addresses.length === 3) {
            setDistrictCodeAddress3(response.data.addresses[2].hamlet.ward.district.code)
            fetchDistrict(response.data.addresses[2].hamlet.ward.district.province.code, 3)
        }
        setWardCodeAddress1(response.data.addresses[0].hamlet.ward.code)
        fetchWard(response.data.addresses[0].hamlet.ward.district.code, 1)
        setWardCodeAddress2(response.data.addresses[1].hamlet.ward.code)
        fetchWard(response.data.addresses[1].hamlet.ward.district.code, 2)
        if (response.data.addresses.length === 3) {
            setWardCodeAddress3(response.data.addresses[2].hamlet.ward.code)
            fetchWard(response.data.addresses[2].hamlet.ward.district.code, 3)
        }
        setShowEditCitizen(true)
        setHamletCodeAddress1(response.data.addresses[0].hamlet.code)
        fetchHamlet(response.data.addresses[0].hamlet.ward.code, 1)
        setHamletCodeAddress2(response.data.addresses[1].hamlet.code)
        fetchHamlet(response.data.addresses[1].hamlet.ward.code, 2)
        if (response.data.addresses.length === 3) {
            setHamletCodeAddress3(response.data.addresses[2].hamlet.code)
            fetchHamlet(response.data.addresses[2].hamlet.ward.code, 3)
        }
        setAssociationList(response.data.associations)
        let lengthAssociation = response.data.associations.length;
        console.log(lengthAssociation)
        for (let i = 0; i < lengthAssociation; i++) {
            console.log(response.data.associations[i].associationType.id)
            if (String(response.data.associations[i].associationType.id) === String(1)) {
                setIdAssociation1(response.data.associations[i].id)
                setFullNameAssociation1(response.data.associations[i].associatedCitizenName)
                setNationalIdAssociation1(response.data.associations[i].associatedCitizenNationalId)
            } else if (String(response.data.associations[i].associationType.id) === String(2)) {
                setIdAssociation2(response.data.associations[i].id)
                setFullNameAssociation2(response.data.associations[i].associatedCitizenName)
                setNationalIdAssociation2(response.data.associations[i].associatedCitizenNationalId)
            } else if (String(response.data.associations[i].associationType.id) === String(3)) {
                setIdAssociation3(response.data.associations[i].id)
                setFullNameAssociation3(response.data.associations[i].associatedCitizenName)
                setNationalIdAssociation3(response.data.associations[i].associatedCitizenNationalId)
            } else if (String(response.data.associations[i].associationType.id) === String(4)) {
                setIdAssociation4(response.data.associations[i].id)
                setFullNameAssociation4(response.data.associations[i].associatedCitizenName)
                setNationalIdAssociation4(response.data.associations[i].associatedCitizenNationalId)
            }
        }
    }

    const ClickFindByNationId = async () => {
        try {
            const response = await axios.get("http://localhost:8080/api/v1/citizen/" + nationalId, config)
            setName(response.data.name)
            setIdCitizen(response.data.nationalId)
            setNationalId(response.data.nationalId)
            setDateOfBirth(response.data.dateOfBirth)
            setSex(response.data.sex)
            setShowTableDetail(true)
            console.log(response.data)
        } catch (error) {
            console.log(error)
        }
    }


    useEffect(() => {
        fetchProvince();
        fetchEthnicity();
        fetchRegion();
        GetDecleration()
    }, [])


    const listEthnicity = ethnicitys.map((post) =>
        <option key={post.id} value={post.id}>{post.id + ". " + post.name}</option>
    );

    const listReligion = religions.map((post) =>
        <option key={post.id} value={post.id}>{post.id + ". " + post.name}</option>
    );

    const listProvincesAddress1 = provincesAddress1.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistrictsAddress1 = districtsAddress1.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWardsAddress1 = wardsAddress1.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listHamletsAddress1 = hamletsAddress1.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
    );

    const listProvincesAddress2 = provincesAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistrictsAddress2 = districtsAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWardsAddress2 = wardsAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listHamletsAddress2 = hamletsAddress2.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
    );

    const listProvincesAddress3 = provincesAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listDistrictsAddress3 = districtsAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listWardsAddress3 = wardsAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.name}</option>
    );

    const listHamletsAddress3 = hamletsAddress3.map((post) =>
        <option key={post.code} value={post.code}>{post.code + ". " + post.administrativeUnit.shortName + " " + post.name}</option>
    );

    const listFindCitizens = listCitizens.map((post, index) =>
        <tr key={index} onClick={() => { GetDataCitizen(post.nationalId) }}>
            <td>{post.nationalId}</td>
            <td>{post.name}</td>
            <td>{post.dateOfBirth}</td>
            <td>{post.sex}</td>
        </tr>
    );

    const ModalEditCitizen = () => {
        return (
            <Modal show={showEditCitizen} size="lg">
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>THAY ĐỔI THÔNG TIN NGƯỜI DÂN</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" value={nationalId} onChange={(e) => {
                                setNationalId(e.target.value)
                            }} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" value={name} onChange={(e) => {
                                setName(e.target.value)
                            }} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Ngày sinh</Form.Label>
                            <Form.Control type="date" value={dateOfBirth} onChange={(e) => setDateOfBirth(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Giới tính</Form.Label>
                            <Form.Select value={sex} onChange={(e) => setSex(e.target.value)}>
                                <option value=""></option>
                                <option value='Nam'>Nam</option>
                                <option value='Nữ'>Nữ</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Nhóm máu</Form.Label>
                            <Form.Select value={bloodType} onChange={(e) => setBloodType(e.target.value)}>
                                <option value=""></option>
                                <option value='A'>A</option>
                                <option value='B'>B</option>
                                <option value='AB'>AB</option>
                                <option value='O'>O</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Tình trạng hôn nhân</Form.Label>
                            <Form.Select value={maritalStatus} onChange={(e) => {
                                setMaritalStatus(e.target.value)
                            }}>
                                <option value=""></option>
                                <option value='Chưa kết hôn'>Chưa kết hôn</option>
                                <option value='Đã kết hôn'>Đã kết hôn</option>
                                <option value='Đã ly hôn'>Đã ly hôn</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Dân tộc</Form.Label>
                            <Form.Select value={ethnicity} onChange={(e) => setEthnicity(e.target.value)}>
                                <option value=""></option>
                                {listEthnicity}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Tôn giáo</Form.Label>
                            <Form.Select value={religionId} onChange={(e) => {
                                setReligionId(e.target.value);
                            }}>
                                <option value=""></option>
                                <option value={0}>0. Không</option>
                                {listReligion}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Quốc tịch khác (nếu có)</Form.Label>
                            <Form.Control type="text" value={otherNationality} onChange={(e) => setOtherNationality(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Nghề nghiệp</Form.Label>
                            <Form.Control type="text" value={job} onChange={(e) => setJob(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Trình độ văn hóa</Form.Label>
                            <Form.Select value={educationalLevel} onChange={(e) => setEducationalLevel(e.target.value)}>
                                <option value=""></option>
                                <option value="Không">0. Không</option>
                                <option value="Tiểu học">1. Tiểu học</option>
                                <option value="Trung học cơ sở">2. Trung học cơ sở</option>
                                <option value="Trung học phổ thông">3. Trung học phổ thông</option>
                                <option value="Trung cấp">4. Trung cấp</option>
                                <option value="Cao đẳng/Đại học">5. Cao đẳng/Đại học</option>
                                <option value="Cao học">6. Cao học</option>
                            </Form.Select>
                        </Form.Group>
                        <div style={{ marginBottom: '10px', fontWeight: 'bold', fontSize: '18px' }}>QUÊ QUÁN</div>
                        <Form.Group className="mb-3">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Select aria-label="Default select example" value={provinceCodeAddress1} onChange={(e) => {
                                fetchDistrict(e.target.value, 1)
                                setProvinceCodeAddress1(e.target.value)
                                setWardsAddress1([])
                                setHamletsAddress1([])
                            }}>
                                <option></option>
                                {listProvincesAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Quận/Huyện/Thị xã</Form.Label>
                            <Form.Select aria-label="Default select example" value={districtCodeAddress1} onChange={(e) => {
                                fetchWard(e.target.value, 1)
                                setDistrictCodeAddress1(e.target.value)
                                setHamletsAddress1([])
                            }}>
                                <option value=""></option>
                                {listDistrictsAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" value={wardCodeAddress1} onChange={(e) => {
                                setWardCodeAddress1(e.target.value, 1)
                                fetchHamlet(e.target.value, 1)
                            }}>
                                <option value=""></option>
                                {listWardsAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" value={hamletCodeAddress1} onChange={(e) => {
                                setHamletCodeAddress1(e.target.value, 1)
                            }}>
                                <option value=""></option>
                                {listHamletsAddress1}
                            </Form.Select>
                        </Form.Group>
                        <div style={{ marginBottom: '10px', fontWeight: 'bold', fontSize: '18px' }}>ĐỊA CHỈ THƯỜNG TRÚ</div>
                        <Form.Group className="mb-3">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Select aria-label="Default select example" value={provinceCodeAddress2} onChange={(e) => {
                                fetchDistrict(e.target.value, 2)
                                setProvinceCodeAddress2(e.target.value)
                                setWardsAddress2([])
                                setHamletsAddress2([])
                            }}>
                                <option></option>
                                {listProvincesAddress2}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Quận/Huyện/Thị xã</Form.Label>
                            <Form.Select aria-label="Default select example" value={districtCodeAddress2} onChange={(e) => {
                                fetchWard(e.target.value, 2)
                                setDistrictCodeAddress2(e.target.value)
                                setHamletsAddress2([])
                            }}>
                                <option value=""></option>
                                {listDistrictsAddress2}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" value={wardCodeAddress2} onChange={(e) => {
                                setWardCodeAddress2(e.target.value, 2)
                                fetchHamlet(e.target.value, 2)
                            }}>
                                <option value=""></option>
                                {listWardsAddress2}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" value={hamletCodeAddress2} onChange={(e) => {
                                setHamletCodeAddress1(e.target.value, 2)
                            }}>
                                <option value=""></option>
                                {listHamletsAddress2}
                            </Form.Select>
                        </Form.Group>
                        <div style={{ marginBottom: '10px', fontWeight: 'bold', fontSize: '18px' }}>ĐỊA CHỈ TẠM TRÚ</div>
                        <Form.Group className="mb-3">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Select aria-label="Default select example" value={provinceCodeAddress3} onChange={(e) => {
                                fetchDistrict(e.target.value, 3)
                                setProvinceCodeAddress3(e.target.value)
                                setWardsAddress3([])
                                setHamletsAddress3([])
                            }}>
                                <option value=""></option>
                                {listProvincesAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Quận/Huyện/Thị xã</Form.Label>
                            <Form.Select aria-label="Default select example" value={districtCodeAddress3} onChange={(e) => {
                                fetchWard(e.target.value, 3)
                                setDistrictCodeAddress3(e.target.value)
                                setHamletsAddress3([])
                            }}>
                                <option value=""></option>
                                {listDistrictsAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" value={wardCodeAddress3} onChange={(e) => {
                                setWardCodeAddress3(e.target.value, 3)
                                fetchHamlet(e.target.value, 3)
                            }}>
                                <option value=""></option>
                                {listWardsAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" value={hamletCodeAddress3} onChange={(e) => {
                                setHamletCodeAddress3(e.target.value, 3)
                            }}>
                                <option value=""></option>
                                {listHamletsAddress3}
                            </Form.Select>
                        </Form.Group>
                        <div style={{ marginBottom: '10px', fontWeight: 'bold', fontSize: '18px' }}>THÔNG TIN NGƯỜI THÂN (BỐ)</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" value={fullNameAssociation1} onChange={(e) => setFullNameAssociation1(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" value={nationalIdAssociation1} onChange={(e) => setNationalIdAssociation1(e.target.value)} />
                        </Form.Group>
                        <div style={{ marginBottom: '10px', fontWeight: 'bold', fontSize: '18px' }}>THÔNG TIN NGƯỜI THÂN (MẸ)</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" value={fullNameAssociation2} onChange={(e) => setFullNameAssociation2(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" value={nationalIdAssociation2} onChange={(e) => setNationalIdAssociation2(e.target.value)} />
                        </Form.Group>
                        <div style={{ marginBottom: '10px', fontWeight: 'bold', fontSize: '18px' }}>THÔNG TIN NGƯỜI THÂN (VỢ/CHỒNG)</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" value={fullNameAssociation3} onChange={(e) => setFullNameAssociation3(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" value={nationalIdAssociation3} onChange={(e) => setNationalIdAssociation3(e.target.value)} />
                        </Form.Group>
                        <div style={{ marginBottom: '10px', fontWeight: 'bold', fontSize: '18px' }}>THÔNG TIN NGƯỜI THÂN (NGƯỜI GIÁM HỘ HỢP PHÁP)</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" value={fullNameAssociation4} onChange={(e) => setFullNameAssociation4(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4" >
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" value={nationalIdAssociation4} onChange={(e) => setNationalIdAssociation4(e.target.value)} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => { setShowEditCitizen(false) }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        UpdateDataCitizen()
                    }}>
                        Xác nhận
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const FirstPageFormInput = () => {
        return (
            <div style={{ margin: '20px 20px 20px 20px', border: '2px solid black' }}>
                <div className='titlePage'>TRANG 1: THÔNG TIN CƠ BẢN CỦA NGƯỜI DÂN</div>
                <div className="listForm">
                    <div className='formFlex'>
                        <Form.Group className="mb-5">
                            <Form.Label>1. Họ và tên</Form.Label>
                            <Form.Control type="text" className="inputForm" value={name} onChange={(e) => setName(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>2. Ngày sinh</Form.Label>
                            <Form.Control type="date" className='inputForm' value={dateOfBirth} onChange={(e) => setDateOfBirth(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>3. Nhóm máu</Form.Label>
                            <Form.Select className='inputForm' value={bloodType} onChange={(e) => setBloodType(e.target.value)}>
                                <option value=""></option>
                                <option value='A'>A</option>
                                <option value='B'>B</option>
                                <option value='AB'>AB</option>
                                <option value='O'>O</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>4. Giới tính</Form.Label>
                            <Form.Select className='inputForm' value={sex} onChange={(e) => setSex(e.target.value)}>
                                <option value=""></option>
                                <option value='Nam'>Nam</option>
                                <option value='Nữ'>Nữ</option>
                            </Form.Select>
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <Form.Group className="mb-5">
                            <Form.Label>5. Tình trạng hôn nhân</Form.Label>
                            <Form.Select className='inputForm' value={maritalStatus} onChange={(e) => setMaritalStatus(e.target.value)}>
                                <option value=""></option>
                                <option value={'Chưa kết hôn'}>Chưa kết hôn</option>
                                <option value={'Đã kết hôn'}>Đã kết hôn</option>
                                <option value={'Đã ly hôn'}>Đã ly hôn</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>6. Dân tộc</Form.Label>
                            <Form.Select className='inputForm' value={ethnicity} onChange={(e) => setEthnicity(e.target.value)}>
                                <option value=""></option>
                                {listEthnicity}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>7. Tôn giáo</Form.Label>
                            <Form.Select className='inputForm' value={religionId} onChange={(e) => {
                                setReligionId(e.target.value);
                            }}>
                                <option value=""></option>
                                {listReligion}
                            </Form.Select>
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <Form.Group className="mb-5">
                            <Form.Label>8. Quốc tịch khác</Form.Label>
                            <Form.Control type="text" className="inputForm" value={otherNationality} onChange={(e) => setOtherNationality(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>9. Nghề nghiệp</Form.Label>
                            <Form.Control type="text" className="inputForm" value={job} onChange={(e) => setJob(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>10. Trình độ văn hóa</Form.Label>
                            <Form.Select className="inputForm" value={educationalLevel} onChange={(e) => setEducationalLevel(e.target.value)}>
                                <option value=""></option>
                                <option value="Tiểu học">1. Tiểu học</option>
                                <option value="Trung học cơ sở">2. Trung học cơ sở</option>
                                <option value="Trung học phổ thông">3. Trung học phổ thông</option>
                                <option value="Trung cấp">4. Trung cấp</option>
                                <option value="Cao đẳng/Đại học">5. Cao đẳng/Đại học</option>
                                <option value="Cao học">6. Cao học</option>
                            </Form.Select>
                        </Form.Group>
                    </div>
                </div>
            </div>
        )
    }

    const SecondPageFormInput = () => {
        return (
            <div style={{ margin: '20px 20px 20px 20px', border: '2px solid black' }}>
                <div className='titlePage'>TRANG 2: THÔNG TIN CƠ BẢN CỦA NGƯỜI DÂN (TIẾP)</div>
                <div className="listForm">
                    <div className='formFlex'>
                        <div className="titleFlex">12. QUÊ QUÁN</div>
                        <Form.Group className="mb-5">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={provinceCodeAddress1} onChange={(e) => {
                                fetchDistrict(e.target.value, 1)
                                setProvinceCodeAddress1(e.target.value)
                                setWardsAddress1([])
                                setHamletsAddress1([])
                            }}>
                                <option></option>
                                {listProvincesAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Quận/Huyện/Thị xã</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={districtCodeAddress1} onChange={(e) => {
                                fetchWard(e.target.value, 1)
                                setDistrictCodeAddress1(e.target.value)
                                setHamletsAddress1([])
                            }}>
                                <option value=""></option>
                                {listDistrictsAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={wardCodeAddress1} onChange={(e) => {
                                setWardCodeAddress1(e.target.value, 1)
                                fetchHamlet(e.target.value, 1)
                            }}>
                                <option value=""></option>
                                {listWardsAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={hamletCodeAddress1} onChange={(e) => {
                                setHamletCodeAddress1(e.target.value, 1)
                            }}>
                                <option value=""></option>
                                {listHamletsAddress1}
                            </Form.Select>
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <div className="titleFlex">13. ĐỊA CHỈ THƯỜNG TRÚ</div>
                        <Form.Group className="mb-5">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={provinceCodeAddress2} onChange={(e) => {
                                fetchDistrict(e.target.value, 2)
                                setProvinceCodeAddress2(e.target.value)
                                setWardsAddress2([])
                                setHamletsAddress2([])
                            }}>
                                <option></option>
                                {listProvincesAddress2}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Quận/Huyện/Thị xã</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={districtCodeAddress2} onChange={(e) => {
                                fetchWard(e.target.value, 2)
                                setDistrictCodeAddress2(e.target.value)
                                setHamletsAddress2([])
                            }}>
                                <option value=""></option>
                                {listDistrictsAddress2}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={wardCodeAddress2} onChange={(e) => {
                                setWardCodeAddress2(e.target.value, 2)
                                fetchHamlet(e.target.value, 2)
                            }}>
                                <option value=""></option>
                                {listWardsAddress2}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={hamletCodeAddress2} onChange={(e) => {
                                setHamletCodeAddress1(e.target.value, 2)
                            }}>
                                <option value=""></option>
                                {listHamletsAddress2}
                            </Form.Select>
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <div className="titleFlex">14. ĐỊA CHỈ TẠM TRÚ</div>
                        <Form.Group className="mb-5">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={provinceCodeAddress3} onChange={(e) => {
                                fetchDistrict(e.target.value, 3)
                                setProvinceCodeAddress3(e.target.value)
                                setWardsAddress3([])
                                setHamletsAddress3([])
                            }}>
                                <option value=""></option>
                                {listProvincesAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Quận/Huyện/Thị xã</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={districtCodeAddress3} onChange={(e) => {
                                fetchWard(e.target.value, 3)
                                setDistrictCodeAddress3(e.target.value)
                                setHamletsAddress3([])
                            }}>
                                <option value=""></option>
                                {listDistrictsAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={wardCodeAddress3} onChange={(e) => {
                                setWardCodeAddress3(e.target.value, 3)
                                fetchHamlet(e.target.value, 3)
                            }}>
                                <option value=""></option>
                                {listWardsAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={hamletCodeAddress3} onChange={(e) => {
                                setHamletCodeAddress3(e.target.value, 3)
                            }}>
                                <option value=""></option>
                                {listHamletsAddress3}
                            </Form.Select>
                        </Form.Group>
                    </div>
                </div>
            </div>
        )
    }

    const ThirdPageFormInput = () => {
        return (
            <div style={{ margin: '20px 20px 20px 20px', border: '2px solid black' }}>
                <div className='titlePage'>TRANG 3: THÔNG TIN CƠ BẢN CỦA NGƯỜI THÂN</div>
                <div className="listForm">
                    <div className='formFlex'>
                        <div className="titleFlex">15. BỐ</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation1} onChange={(e) => setFullNameAssociation1(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" className='inputForm' value={nationalIdAssociation1} onChange={(e) => setNationalIdAssociation1(e.target.value)} />
                        </Form.Group>
                        <div className="titleFlex">16. MẸ</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation2} onChange={(e) => setFullNameAssociation2(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" className='inputForm' value={nationalIdAssociation2} onChange={(e) => setNationalIdAssociation2(e.target.value)} />
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <div className="titleFlex">17. VỢ/CHỒNG</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation3} onChange={(e) => setFullNameAssociation3(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" className='inputForm' value={nationalIdAssociation3} onChange={(e) => setNationalIdAssociation3(e.target.value)} />
                        </Form.Group>
                        <div className="titleFlex">18. NGƯỜI GIÁM HỘ HỢP PHÁP</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation4} onChange={(e) => setFullNameAssociation4(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4" >
                            <Form.Label>Số định danh cá nhân</Form.Label>
                            <Form.Control type="text" className='inputForm' value={nationalIdAssociation4} onChange={(e) => setNationalIdAssociation4(e.target.value)} />
                        </Form.Group>
                    </div>
                </div>
            </div>
        )
    }

    const RowTableFindbyNationalId = () => {
        return (
            <tr onClick={() => {
                setShowEditCitizen(true)
                GetDataCitizen(nationalId)
            }}>
                <td>{nationalId}</td>
                <td>{name}</td>
                <td>{dateOfBirth}</td>
                <td>{sex}</td>
            </tr>
        )
    }

    const TableResidentialInHalmet = () => {
        return (
            <div style={{ marginTop: '20px' }}>
                <div>
                    <Table striped bordered hover size="sm" className="tableResidentialHamlet">
                        <thead>
                            <tr>
                                <th>CMMD/CCCD</th>
                                <th>Họ và tên</th>
                                <th>Ngày sinh</th>
                                <th>Giới tính</th>
                            </tr>
                        </thead>
                        <tbody>
                            {(firstSelectOption) ? RowTableFindbyNationalId() : null }
                            {(secondSelectOption) ? listFindCitizens : null}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }

    const TreeUnits = () => {
        return (
            <div className="FindCitizenOption" style={{ height: heightScreen }}>
                <div className="buttonOption" style={(firstSelectOption) ? { backgroundColor: 'yellow' } : null} onClick={() => {
                    setFirstSelectOption(!firstSelectOption)
                    setSecondSelectOption(false)
                    setShowOption(false)
                    setShowTableDetail(false)
                }}><div className="titleOption">Tìm kiếm định danh</div></div>
                <div className="buttonOption" style={(secondSelectOption) ? { backgroundColor: 'yellow' } : null} onClick={() => {
                    setSecondSelectOption(!secondSelectOption)
                    setFirstSelectOption(false)
                    setShowOption(!showOption)
                    setName('')
                    setIdCitizen('')
                    setSex('')
                    setDateOfBirth('')
                    setJob('')
                    setProvinceCodeAddress1('')
                    setProvinceCodeAddress2('')
                    setProvinceCodeAddress3('')
                    setDistrictCodeAddress1('')
                    setDistrictCodeAddress2('')
                    setDistrictCodeAddress3('')
                    setWardCodeAddress1('')
                    setWardCodeAddress2('')
                    setWardCodeAddress3('')
                    setHamletCodeAddress1('')
                    setHamletCodeAddress2('')
                    setHamletCodeAddress3('')
                    setIdAssociation1(null)
                    setNationalIdAssociation1('')
                    setFullNameAssociation1('')
                    setIdAssociation2(null)
                    setNationalIdAssociation2('')
                    setFullNameAssociation2('')
                    setIdAssociation3(null)
                    setNationalIdAssociation3('')
                    setFullNameAssociation3('')
                    setIdAssociation4(null)
                    setNationalIdAssociation4('')
                    setFullNameAssociation4('')
                    setEducationalLevel('')
                    setBloodType('')
                    setOtherNationality('')
                    setEthnicity('')
                    setMaritalStatus('')
                    setReligionId('')
                    setNationalId('')
                    setShowTableDetail(false)
                }}><div className="titleOption">Tìm kiếm chung</div></div>
                <div className="noteFindCitizen">⁕ Tìm kiếm định danh là tìm kiếm dựa trên số định danh cá nhân của người dân và chỉ trả về một kết quả duy nhất</div>
                <div className="noteFindCitizen">⁕ Tìm kiếm chung là tìm kiếm dựa trên các trường còn lại, các trường không có thông tin có thể bỏ trống và kết quả trả về có thể định danh cá nhân hoặc danh sách người dân</div>
            </div>
        )
    }

    const ListOptionButton = () => {
        return (
            <div>
                <div className='pagelistButton'>
                    <Button className={(page === 1) ? "optionButtonSelect" : "optionButton"} onClick={() => setPage(1)}>Trang 1</Button>
                    <Button className={(page === 2) ? "optionButtonSelect" : "optionButton"} onClick={() => setPage(2)}>Trang 2</Button>
                    <Button className={(page === 3) ? "optionButtonSelect" : "optionButton"} onClick={() => setPage(3)}>Trang 3</Button>
                    <Button className="optionButton" onClick={() => {FindCitizenByMultiInfo()}}>Tìm kiếm</Button>
                </div>
            </div>
        )
    }

    const FormInputNationalId = () => {
        return (
            <div className="flexOptionNationalId">
                <Form.Group>
                    <Form.Label className="titleOptionNationalId">Số định danh cá nhân:</Form.Label>
                    <input type="text" value={nationalId} onChange={(e) => setNationalId(e.target.value)} />
                    <Button className='buttonInput' onClick={() => ClickFindByNationId()}>Tìm kiếm</Button>
                </Form.Group>
            </div>

        )
    }


    const FormInput = () => {
        return (
            <div>
                <div className="inputOptions">
                    <ListOptionButton />
                    <div>
                        {(page === 2) ? SecondPageFormInput() : null}
                        {(page === 3) ? ThirdPageFormInput() : null}
                        {(page === 1) ? FirstPageFormInput() : null}
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div>
            <NavbarPage />
            <div className="flexFindCitizen">
                <TreeUnits className="listOptions" />
                <div className="inputOptions">
                    {(showOption) ? FormInput() : null}
                    {(firstSelectOption) ? FormInputNationalId() : null}
                    {(showTableDetail) ? TableResidentialInHalmet() : null}
                </div>
            </div>
            {ModalEditCitizen()}
        </div>
    );
}

export default FindCititzen;
