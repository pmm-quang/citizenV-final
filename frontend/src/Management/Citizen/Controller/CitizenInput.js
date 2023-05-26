import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../../../Navbar/NavbarPage.js';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';
import PDFFile from '../file/mau_phieu.pdf'
import '../css/CitizenInput.css'
import ExcelFile from '../file/mau_excel.xlsx'
import { useState, useEffect } from 'react';
import Modal from 'react-bootstrap/Modal';

function CitizenInput() {
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
    const [provincesAddress3, setProvincesAddress3] = useState([]);
    const [districtsAddress1, setDistrictsAddress1] = useState([]);
    const [districtsAddress3, setDistrictsAddress3] = useState([]);
    const [wardsAddress1, setWardsAddress1] = useState([]);
    const [wardsAddress3, setWardsAddress3] = useState([]);
    const [hamletsAddress1, setHamletsAddress1] = useState([]);
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
    const [declaration, setDeclaration] = useState()
    const [file, setFile] = useState()
    

    const GetDecleration = async () => {
        try {
            const response = await axios("http://localhost:8080/api/v1/user/" + user, config)
            setDeclaration(response.data.declaration)
        } catch (err) {
            console.error(err);
        }
    }

    const fetchNameAddress = async () => {
        try {
            const response_province = await axios("http://localhost:8080/api/v1/province/" + provinceCodeAddress2, config)
            setNameProvinceAddress2(response_province.data.name)
            const response_district = await axios('http://localhost:8080/api/v1/district/' + districtCodeAddress2, config);
            setNameDistrictAddress2(response_district.data.name)
            console.log(response_district.data)
            const response_ward = await axios("http://localhost:8080/api/v1/ward/" + wardCodeAddress2, config)
            setNameWardAddress2(response_ward.data.name)
            const response_hamlet = await axios("http://localhost:8080/api/v1/hamlet/" + hamletCodeAddress2, config)
            setNameHamletAddress2(response_hamlet.data.administrativeUnit.shortName + " " + response_hamlet.data.name)
        } catch (err) {
            console.error(err);
        }
    }

    // get list of provinces
    const fetchProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/', config);
            setProvincesAddress1(response.data);
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

    const OnClickButton = async () => {
        const addresses = [
            {
                "addressType": 1,
                "hamletCode": hamletCodeAddress1,
                "provinceCode": provinceCodeAddress1,
            },
            {
                "addressType": 2,
                "hamletCode": hamletCodeAddress2,
                "provinceCode": provinceCodeAddress2,
            }
        ]

        if (hamletCodeAddress3 !== "") {
            addresses.push({
                "addressType": 3,
                "hamletCode": hamletCodeAddress3,
                "provinceCode": provinceCodeAddress3,
            })
        }

        const citizen = {
            "nationalId": nationalId,
            "name": fullName,
            "dateOfBirth": dateOfBirth,
            "bloodType": bloodType,
            "sex": sex,
            "maritalStatus": marriageStatus,
            "ethnicityId": Number(ethnicity),
            "otherNationality": (otherNationality === "") ? null : otherNationality,
            "religion": (religionId === "0") ? null : {
                "id": religionId,
                "name": religionName
            },
            "addresses": addresses,
            "associations": [
                {
                    "id": null,
                    "associatedCitizenNationalId": (nationalIdAssociation1 === "") ? null : nationalIdAssociation1,
                    "associatedCitizenName": (fullNameAssociation1 === "") ? null : fullNameAssociation1,
                    "associationType": {
                        "id": 1,
                        "name": "Cha-con"
                    }
                },
                {
                    "id": null,
                    "associatedCitizenNationalId": (nationalIdAssociation2 === "") ? null : nationalIdAssociation2,
                    "associatedCitizenName": (fullNameAssociation2 === "") ? null : fullNameAssociation2,
                    "associationType": {
                        "id": 2,
                        "name": "Mẹ-con"
                    }
                },
                {
                    "id": null,
                    "associatedCitizenNationalId": (nationalIdAssociation3 === "") ? null : nationalIdAssociation3,
                    "associatedCitizenName": (fullNameAssociation3 === "") ? null : fullNameAssociation3,
                    "associationType": {
                        "id": 3,
                        "name": "Chồng-vợ"
                    }
                },
                {
                    "id": null,
                    "associatedCitizenNationalId": (nationalIdAssociation4 === "") ? null : nationalIdAssociation4,
                    "associatedCitizenName": (fullNameAssociation4 === "") ? null : fullNameAssociation4,
                    "associationType": {
                        "id": 4,
                        "name": "Người đại diện hợp pháp"
                    }
                }
            ]
        }
        console.log(citizen)
        if (nationalId === "" || fullName === "" || hamletCodeAddress1 === "" || hamletCodeAddress2 === "" || sex === "" || bloodType === "" || marriageStatus === "" || ethnicity === "" || dateOfBirth === "") {
            setChecked(false)
        } else {
            setChecked(true)
            await axios.post("http://localhost:8080/api/v1/citizen/save", citizen, config)
        }
        setShowNotify(true)
    }

    // get religion's name from religion's id
    const setNameReligion = (id) => {
        if (id === 0) setReligionName('Không có')
        else {
            for (let i = 0; i < religions.length; i++) {
                if (religions[i].id === Number(id)) {
                    setReligionName(religions[i].name)
                }
            }
        }
    }

    // post file Excel 
    const PostFileExcel = async () => {
        let formData = new FormData()
        let reader = new FileReader()
        formData.append("excelFile", file)
        const config = {
            headers: {
                "Content-Type": "multipart/form-data",
                "Authorization": `Bearer ${user_account.accessToken}`
              },
        }
        console.log(config)
        try {
            const response = await axios.post("http://localhost:8080/api/v1/citizen/excel/upload", formData, config)
            console.log(response.data)
        } catch (error) {
            console.log(error)
        }
    }

    
    const SetDefault = () => {
        setProvinceCodeAddress1("")
        setDistrictCodeAddress1("");
        setWardCodeAddress1("");
        setHamletCodeAddress1("");
        setProvinceCodeAddress3("");
        setDistrictCodeAddress3("");
        setWardCodeAddress3("");
        setHamletCodeAddress3("");
        setDistrictsAddress1([]);
        setDistrictsAddress3([]);
        setWardsAddress1([]);
        setWardsAddress3([]);
        setHamletsAddress1([]);
        setHamletsAddress3([]);
        setBloodType("");
        setDateOfBirth("");
        setEthnicity("");
        setSex("");
        setMarriageStatus("");
        setReligionId("");
        setReligionName("");
        setFullName("");
        setOtherNationality("")
        setNationalId("")
        setNationalIdAssociation1("")
        setNationalIdAssociation2("")
        setNationalIdAssociation3("")
        setNationalIdAssociation4("")
        setFullNameAssociation1("")
        setFullNameAssociation2("")
        setFullNameAssociation3("")
        setFullNameAssociation4("")
    }



    useEffect(() => {
        fetchProvince();
        fetchEthnicity();
        fetchRegion();
        fetchNameAddress();
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

    const FirstPageFormInput = () => {
        return (
            <div style={{ margin: '20px 20px 20px 20px', border: '2px solid black' }}>
                <div className='titlePage'>TRANG 1: THÔNG TIN CƠ BẢN CỦA NGƯỜI DÂN</div>
                <div className="listForm">
                    <div className='formFlex'>
                        <Form.Group className="mb-5">
                            <Form.Label>1*. Họ và tên</Form.Label>
                            <Form.Control type="text" className="inputForm" value={fullName} onChange={(e) => setFullName(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>2*. Số CMND/CCCD</Form.Label>
                            <Form.Control type="text" className="inputForm" value={nationalId} onChange={(e) => setNationalId(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>3*. Ngày sinh</Form.Label>
                            <Form.Control type="date" className='inputForm' value={dateOfBirth} onChange={(e) => setDateOfBirth(e.target.value)} />
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <Form.Group className="mb-5">
                            <Form.Label>4*. Nhóm máu</Form.Label>
                            <Form.Select className='inputForm' value={bloodType} onChange={(e) => setBloodType(e.target.value)}>
                                <option></option>
                                <option value='A'>A</option>
                                <option value='B'>B</option>
                                <option value='AB'>AB</option>
                                <option value='O'>O</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>5*. Giới tính</Form.Label>
                            <Form.Select className='inputForm' value={sex} onChange={(e) => setSex(e.target.value)}>
                                <option></option>
                                <option value='Nam'>Nam</option>
                                <option value='Nữ'>Nữ</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>6*. Tình trạng hôn nhân</Form.Label>
                            <Form.Select className='inputForm' value={marriageStatus} onChange={(e) => setMarriageStatus(e.target.value)}>
                                <option></option>
                                <option value={'Chưa kết hôn'}>Chưa kết hôn</option>
                                <option value={'Đã kết hôn'}>Đã kết hôn</option>
                                <option value={'Đã ly hôn'}>Đã ly hôn</option>
                            </Form.Select>
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <Form.Group className="mb-5">
                            <Form.Label>7*. Dân tộc</Form.Label>
                            <Form.Select className='inputForm' value={ethnicity} onChange={(e) => setEthnicity(e.target.value)}>
                                <option></option>
                                {listEthnicity}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>8*. Tôn giáo</Form.Label>
                            <Form.Select className='inputForm' value={religionId} onChange={(e) => {
                                setReligionId(e.target.value);
                                setNameReligion(e.target.value);
                            }}>
                                <option></option>
                                <option value={0}>0. Không có</option>
                                {listReligion}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>9. Quốc tịch khác (nếu có)</Form.Label>
                            <Form.Control type="text" className="inputForm" value={otherNationality} onChange={(e) => setOtherNationality(e.target.value)} />
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
                        <div className="titleFlex">10*. QUÊ QUÁN</div>
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
                                <option></option>
                                {listDistrictsAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={wardCodeAddress1} onChange={(e) => {
                                setWardCodeAddress1(e.target.value, 1)
                                fetchHamlet(e.target.value, 1)
                            }}>
                                <option></option>
                                {listWardsAddress1}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={hamletCodeAddress1} onChange={(e) => {
                                setHamletCodeAddress1(e.target.value, 1)
                            }}>
                                <option></option>
                                {listHamletsAddress1}
                            </Form.Select>
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <div className="titleFlex">11. ĐỊA CHỈ THƯỜNG TRÚ</div>
                        <Form.Group className="mb-5">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Control aria-label="Default select example" className='inputForm' value={provinceCodeAddress2 + ". " + nameProvinceAddress2} disabled />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Quận/Huyện/Thị xã</Form.Label>
                            <Form.Control aria-label="Default select example" className='inputForm' value={districtCodeAddress2 + ". " + nameDistrictAddress2} disabled />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Control aria-label="Default select example" className='inputForm' value={wardCodeAddress2 + ". " + nameWardAddress2} disabled
                            />
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Control aria-label="Default select example" className='inputForm' value={hamletCodeAddress2 + ". " + nameHamletAddress2} disabled
                            />
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <div className="titleFlex">12. ĐỊA CHỈ TẠM TRÚ</div>
                        <Form.Group className="mb-5">
                            <Form.Label>Tỉnh / Thành phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={provinceCodeAddress3} onChange={(e) => {
                                fetchDistrict(e.target.value, 3)
                                setProvinceCodeAddress3(e.target.value)
                                setWardsAddress3([])
                                setHamletsAddress3([])
                            }}>
                                <option></option>
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
                                <option></option>
                                {listDistrictsAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Xã/Phường/Thị trấn</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={wardCodeAddress3} onChange={(e) => {
                                setWardCodeAddress3(e.target.value, 3)
                                fetchHamlet(e.target.value, 3)
                            }}>
                                <option></option>
                                {listWardsAddress3}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-5">
                            <Form.Label>Thôn/Xóm/Tổ dân phố</Form.Label>
                            <Form.Select aria-label="Default select example" className='inputForm' value={hamletCodeAddress3} onChange={(e) => {
                                setHamletCodeAddress3(e.target.value, 3)
                            }}>
                                <option></option>
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
                        <div className="titleFlex">13. BỐ</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation1} onChange={(e) => setFullNameAssociation1(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số CMND/CCCD</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation1} onChange={(e) => setNationalIdAssociation1(e.target.value)} />
                        </Form.Group>
                        <div className="titleFlex">14. MẸ</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation2} onChange={(e) => setFullNameAssociation2(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số CMND/CCCD</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation2} onChange={(e) => setNationalIdAssociation2(e.target.value)} />
                        </Form.Group>
                    </div>
                    <div className='formFlex'>
                        <div className="titleFlex">15. VỢ/CHỒNG</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation3} onChange={(e) => setFullNameAssociation3(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4">
                            <Form.Label>Số CMND/CCCD</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation3} onChange={(e) => setNationalIdAssociation3(e.target.value)} />
                        </Form.Group>
                        <div className="titleFlex">16. NGƯỜI GIÁM HỘ HỢP PHÁP</div>
                        <Form.Group className="mb-4">
                            <Form.Label>Họ và tên</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation4} onChange={(e) => setFullNameAssociation3(e.target.value)} />
                        </Form.Group>
                        <Form.Group className="mb-4" >
                            <Form.Label>Số CMND/CCCD</Form.Label>
                            <Form.Control type="text" className='inputForm' value={fullNameAssociation4} onChange={(e) => setNationalIdAssociation3(e.target.value)} />
                        </Form.Group>
                    </div>
                </div>
            </div>
        )
    }

    // Show message out of declared time
    const NotifyDeclaration = () => {
        return (
            <div>
                <div className="warning">HIỆN KHÔNG PHẢI THỜI GIAN KHAI BÁO DÂN SỐ</div>
                <div className="childWarning">Vui lòng quay trở lại sau</div>
            </div>
        )
    }

    // Show message completed declaration
    const NotifyCompleteDeclaration = () => {
        return (
            <div>
                <div className="successNotify">ĐÃ HOÀN THÀNH KHAI BÁO</div>
                <div className="childSuccessNotify">Vui lòng quay trở lại sau</div>
            </div>
        )
    }

    const ModalNotify = () => {
        return (
            <Modal show={showNotify}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal' style={{ color: (checked) ? 'green' : 'red', fontWeight: 'bold' }}>{(checked) ? "THÊM NGƯỜI DÂN THÀNH CÔNG" : "THÊM NGƯỜI DÂN KHÔNG THÀNH CÔNG"}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {(checked) ? "Bạn có thể kiểm tra thông tin người dân vừa nhập bằng cách sử dụng chức năng tìm kiếm" : "Hãy nhập đầy đủ thông tin ở các trường bắt buộc"}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowNotify(false)
                    }}>
                        Đóng
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    // form input excel
    const ModalInputByExcel = () => {
        return (
            <Modal show={showInput}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>NHẬP DỮ LIỆU TỪ FILE</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Control type="file" accept=".xlsx, .xls, .csv" onChange={(e) => {
                            setFile(e.target.files[0])}
                        } />
                    </Form>
                    <div className="noteInputbyfile">Lưu ý: Chỉ chấp nhận các định dạng: ".xlsx", ".xls", ".csv"</div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowInput(false)
                    }}>
                        Đóng
                    </Button>
                    <Button variant="secondary" onClick={() => {
                        setShowInput(false)
                        PostFileExcel()
                    }}>
                        Xác nhận
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const ListOptionButton = () => {
        return (
            <div>
                <div className='pagelistButton'>
                    <Button className={(page === 1) ? "optionButtonSelect" : "optionButton"} onClick={() => setPage(1)}>Trang 1</Button>
                    <Button className={(page === 2) ? "optionButtonSelect" : "optionButton"} onClick={() => setPage(2)}>Trang 2</Button>
                    <Button className={(page === 3) ? "optionButtonSelect" : "optionButton"} onClick={() => setPage(3)}>Trang 3</Button>
                </div>
                <div className='listButton'>
                    <Button className="optionButton" onClick={() => OnClickButton()}>Xác nhận</Button>
                    <Button className="optionButton" onClick={() => SetDefault()}>Xóa toàn bộ dữ liệu</Button>
                    <Button className="optionButton" onClick={() => setShowInput(true)}>Nhập bằng tệp</Button>
                    <a href={PDFFile} download="mau-phieu-thu-thap-thong-tin-dan-cu" target="_blank" rel="noreferrer">
                        <Button className="optionButton">Tải mẫu phiếu</Button>
                    </a>
                    <a href={ExcelFile} download="mau-file-du-lieu-dan-cu" target="_blank" rel="noreferrer">
                        <Button className="optionButton">Tải file mẫu</Button>
                    </a>
                </div>
            </div>
        )
    }

    const FormInput = () => {
        return (
            <div>
                <div>
                    <ListOptionButton />
                </div>
                {(page === 1) ? FirstPageFormInput() : null}
                {(page === 2) ? SecondPageFormInput() : null}
                {(page === 3) ? ThirdPageFormInput() : null}
                <ModalNotify />
                {ModalInputByExcel()}
            </div>
        )
    }

    return (
        <div>
            <NavbarPage />
            {(status === "Đang khai báo") ? FormInput() : null}
            {(status === "Đã hoàn thành") ? <NotifyCompleteDeclaration /> : null}
            {(status === "Chưa cấp quyền khai báo") ? <NotifyDeclaration /> : null}
        </div>
    );
}

export default CitizenInput;
