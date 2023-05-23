import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import './province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';
import { BiCheckCircle } from 'react-icons/bi'

function District() {
    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.info.username;

    const [showWarning, setShowWarning] = useState(false)
    const [districts, setDistricts] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [province, setProvince] = useState('');
    const [nameDistrict, setNameDistrict] = useState('');
    const [idDistrict, setIdDistrict] = useState('');
    const [defaultIdDistrict, setDefaultIdDistrict] = useState('');
    const [idUnitDistrict, setIdUnitDistrict] = useState('');
    const [checkedId, setCheckedId] = useState(-1);
    const [showWarningCreate, setWarningCreate] = useState(false);

    const config = {
        headers: {
            Authorization: `Bearer ${user_account.accessToken}`
        },
    };

    const fetchFullDistrict = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/by-province/' + user, config);
            setDistricts(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetaildistrict = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/district/' + code, config);
            setIdDistrict(response.data.code)
            setNameDistrict(response.data.name)
            setIdUnitDistrict(response.data.administrativeUnit.id)
            setProvince(response.data.province)
            setShowEdit(true)
            setDefaultIdDistrict(response.data.code)
        } catch (err) {
            console.error(err);
        }
    };

    const EditDistrict = async (code) => {
        setCheckedId(-1)
        const district = {
            code: idDistrict,
            name: nameDistrict,
            administrativeUnit: {},
            province: province
        }
        const response = await axios.get("http://localhost:8080/api/v1/administrativeUnit/" + idUnitDistrict, config)
        district.administrativeUnit = response.data
        console.log(district)
        try {
            await axios.put("http://localhost:8080/api/v1/district/save/" + defaultIdDistrict, district, config)
            setShowWarning(false)
            setShowEdit(false)
            const response = await axios('http://localhost:8080/api/v1/district/by-province/' + user, config);
            setDistricts(response.data);
        } catch {
            setShowWarning(true)
        }
    }

    const CreateNewDistrict = async (code) => {
        const district = {
            code: idDistrict,
            name: nameDistrict,
            administrativeUnit: {},
            province: {}
        }
        if (checkedId === 1 && idDistrict !== '' && idUnitDistrict !== '') {
            try {
                const respons_administrativeUnit = await axios.get("http://localhost:8080/api/v1/administrativeUnit/" + idUnitDistrict, config)
                district.administrativeUnit = respons_administrativeUnit.data
                const respons_province = await axios.get("http://localhost:8080/api/v1/province/" + user, config)
                district.province = respons_province.data
                await axios.post("http://localhost:8080/api/v1/district/save", district, config)
                setWarningCreate(false)
                setShowEdit(false)
                const response = await axios('http://localhost:8080/api/v1/district/by-province/' + user, config);
                setDistricts(response.data);
                setShow(false)
            } catch {
                setShowWarning(true)
            }
        } else {
            console.log('error')
            setWarningCreate(true)
        }
    }

    const CheckedIdNewDistrict = async () => {
        setCheckedId(0)
        if (idDistrict.substring(0, 2) !== user || idDistrict.length < 4) setCheckedId(3)
        else {
            try {
                const response = await axios.get("http://localhost:8080/api/v1/district/" + idDistrict, config)
                setCheckedId(2)
            } catch {
                setCheckedId(1)
            }
        }
    }

    useEffect(() => {
        fetchFullDistrict();
    }, [])

    const formatData = (districts) => {
        return districts.map((item) => ({ ...item, isActive: false }))
    }

    const CreateDistrict = () => {
        setShow(true)
        setIdDistrict()
        setNameDistrict()
        setIdUnitDistrict()
        setCheckedId(-1)
    }

    const ModalDistrict = () => {
        return (
            <Modal show={show}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Khai báo Quận/Huyện/Thị xã</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên Quận/Huyện/Thị xã (*)</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameDistrict}
                                onChange={(e) => {
                                    setNameDistrict(e.target.value)
                                    setWarningCreate(false)
                                }
                                }
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Cấp mã cho Quận/Huyện/Thị xã (**)</Form.Label>
                            <Form.Control
                                type="text"
                                value={idDistrict}
                                onChange={(e) => {
                                    setIdDistrict(e.target.value)
                                    setCheckedId(0)
                                    setWarningCreate(false)
                                    if (e.target.value.length === 0) setCheckedId(-1)
                                }}
                            />
                            {(checkedId === 0) ? <div className='checked' onClick={() => CheckedIdNewDistrict()}><BiCheckCircle className="iconChecked" />Kiểm tra</div> : null}
                            {(checkedId === 2) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập đang bị trùng với một đơn vị hành chính đã có sẵn</div> : null}
                            {(checkedId === 3) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập không nằm trong khu vực quản lý</div> : null}
                            {(checkedId === 1) ? <div className="successChecked">Bạn có thể sử dụng mã vừa nhập</div> : null}
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select value={idUnitDistrict} onChange={(e) => {
                                setIdUnitDistrict(e.target.value)
                                setWarningCreate(false)
                            }}><option></option><option value={4}>1. Thành phố trực thuộc tỉnh</option><option value={5}>2. Quận</option><option value={6}>3. Huyện</option><option value={7}>4. Thị xã</option></Form.Select>
                        </Form.Group>
                    </Form>
                    {(showWarningCreate) ? <div className="noteWarning"><p>THÊM MỚI KHÔNG THÀNH CÔNG</p></div> : null}
                </Modal.Body>

                <Modal.Footer>
                    <div className="note">
                        <p>(*) Tên quận/huyện/thị xã không được trùng lặp với tên quận/huyện/thị xã đã được khai báo</p>
                        <p>(**) Mã số của quận/huyện/thị xã không được trùng lặp với mã số của quận/huyện/thị xã đã được khai báo</p>
                    </div>
                    <Button variant="secondary" onClick={() => { setShow(false) }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        CreateNewDistrict()
                    }}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const ModalEditDistrict = () => {
        return (
            <Modal show={showEdit}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Cập nhật thông tin của quận/huyện/thị xã</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên quận/huyện/thị xã</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameDistrict}
                                onChange={(e) => {
                                    setNameDistrict(e.target.value)
                                }
                                }
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã Quận/Huyện/Thị xã</Form.Label>

                            <Form.Control
                                type="number"
                                value={idDistrict}
                                onChange={(e) => {
                                    setIdDistrict(e.target.value)
                                    setCheckedId(0)
                                }}
                            />
                            {(checkedId === 0) ? <div className='checked' onClick={() => CheckedIdNewDistrict()}><BiCheckCircle className="iconChecked" />Kiểm tra</div> : null}
                            {(checkedId === 2) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập đang bị trùng với một đơn vị hành chính đã có sẵn</div> : null}
                            {(checkedId === 3) ? <div className="warningChecked">Mã của đơn vị hành chính vừa nhập không nằm trong khu vực quản lý</div> : null}
                            {(checkedId === 1) ? <div className="successChecked">Bạn có thể sử dụng mã vừa nhập</div> : null}
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Phân loại đơn vị hành chính</Form.Label>
                            <Form.Select value={idUnitDistrict} onChange={(e) => {
                                setIdUnitDistrict(e.target.value)
                           
                            }}
                            >
                                <option></option><option value={4}>1. Thành phố trực thuộc tỉnh</option><option value={5}>2. Quận</option><option value={6}>3. Thị xã</option><option value={7}>4. Huyện</option></Form.Select>
                        </Form.Group>
                    </Form>
                    {(showWarning) ? <div className="noteWarning"><p>THAY ĐỔI THÔNG TIN KHÔNG THÀNH CÔNG</p></div> : null}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        districts.map((item) => {
                            if (item.code === idDistrict) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => {
                        EditDistrict()
                        setShowEdit(false)
                        districts.map((item) => {
                            if (item.code === idDistrict) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal >
        )
    }

    const listDistricts = districts.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetaildistrict(post.code)
            post.isActive = true
        }} className="rowTable" style={{ backgroundColor: (post.isActive) ? "yellow" : "white" }}>
            <td>{post.code}</td>
            <td>{post.name}</td>
            <td>{post.administrativeUnit.fullName}</td>
        </tr>
    );

    const TableResidential = () => {
        return (
            <div>
                <div>
                    <Table striped bordered hover size="sm" className="tableResidential">
                        <thead>
                            <tr>
                                <th>Mã</th>
                                <th>Tên đơn vị</th>
                                <th>Phân loại đơn vị hành chính</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listDistricts}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <Button className="buttonAdd" onClick={() => {
                setShow(true)
                setWarningCreate(false)
                CreateDistrict()
            }}>+ Khai báo quận/huyện/thị xã</Button>

            <div>
                <TableResidential />
                {(show) ? ModalDistrict() : null}
                {ModalEditDistrict()}
            </div>
        </div>
    );
}

export default District;
