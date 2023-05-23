import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Button from 'react-bootstrap/Button'
import './province.css'
import { useState, useEffect } from 'react';
import Table from 'react-bootstrap/Table';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import Modal from 'react-bootstrap/Modal';
import { BsChevronRight, BsChevronDoubleRight, BsChevronDoubleLeft, BsChevronLeft } from 'react-icons/bs'
import { BiCheckCircle } from 'react-icons/bi'

function Province() {
    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.username;
    const config = {
        headers: {
            Authorization: `Bearer ${user_account.accessToken}`
        },
    };

    const [provinces, setProvinces] = useState([]);
    const [show, setShow] = useState(false);
    const [showEdit, setShowEdit] = useState(false);
    const [regions, setRegions] = useState([]);
    const [nameProvince, setNameProvince] = useState();
    const [idProvince, setIdProvince] = useState();
    const [defalutIdProvince, setDefaultIdProvince] = useState();
    const [regionProvince, setRegionProvince] = useState();
    const [unitProvince, setUnitProvince] = useState();
    const [page, setPage] = useState(1);
    const [numberPage, setNumberPage] = useState(0);
    const [administrativeCode, setAdministrativeCode] = useState()
    const [administrativeUnitName, setAdministrativeUnitName] = useState();
    const [administrativeUnitShortName, setAdministrativeUnitShortName] = useState();
    const [administrativeRegionName, setAdministrativeRegionName] = useState();
    const [code, setCode] = useState()
    const [showWarning, setShowWarning] = useState(false)
    const [checkedId, setCheckedId] = useState(-1);
    const [showWarningCreate, setWarningCreate] = useState(0);

    const fetchFullProvince = async (page) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/?page=' + page, config);
            setProvinces(response.data.provinces);
            console.log(provinces)
            setNumberPage(response.data.totalPages)
        } catch (err) {
            console.error(err);
        }
    };

    const fetchFullRegions = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/administrativeRegion/', config);
            setRegions(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const fetchDetailProvince = async (code) => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/' + code, config);
            setIdProvince(response.data.code)
            setCode(response.data.code)
            setNameProvince(response.data.name)
            setRegionProvince(response.data.administrativeRegion.id)
            setAdministrativeRegionName(response.data.administrativeRegion.name)
            setUnitProvince(response.data.administrativeUnit.id)
            setAdministrativeUnitShortName(response.data.administrativeUnit.shortName)
            setAdministrativeUnitName(response.data.administrativeUnit.fullName)
            setAdministrativeCode(response.data.administrativeUnit.name)
            console.log(unitProvince)
            setAdministrativeCode(response.data.administrativeCode)
            setDefaultIdProvince(response.data.code)
            setShowEdit(true)
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchFullProvince(1);
        fetchFullRegions();
    }, [])

    const listRegionItems = regions.map((region) =>
        <option key={region.id} value={region.id}>{region.id + ". " + region.name}</option>
    )

    const formatData = (provinces) => {
        return provinces.map((item) => ({ ...item, isActive: false }))
    }

    const CreateProvince = () => {
        setShow(true)
        setIdProvince('')
        setNameProvince('')
        setRegionProvince('')
        setUnitProvince('')
        setAdministrativeCode('')
        setCheckedId(-1)
    }

    const CreateNewProvince = async () => {
        const province = {
            "code": idProvince,
            "name": nameProvince,
            "administrativeUnit": {
                "id": unitProvince,
                "fullName": administrativeUnitName,
                "shortName": administrativeUnitShortName
            },
            "administrativeRegion": {
                "id": regionProvince,
                "name": administrativeRegionName
            },
            "administrativeCode": administrativeCode
        }

        if (String(unitProvince) === '1') {
            province.administrativeUnit.fullName = "Thành phố trực thuộc trung ương";
            province.administrativeUnit.shortName = "Thành phố";
        } else if (String(unitProvince) === '2') {
            province.administrativeUnit.fullName = "Tỉnh";
            province.administrativeUnit.shortName = "Tỉnh";
        }
        for (let i = 0; i < regions.length; i++) {
            if (regionProvince === String(regions[i].id)) {
                province.administrativeRegion.name = regions[i].name
            }
        }
        try {
            await axios.post("http://localhost:8080/api/v1/province/save", province, config)
            const response = await axios('http://localhost:8080/api/v1/province/?page=' + page, config);
            setProvinces(response.data.provinces);
            setShow(false)
            setWarningCreate(false)
        }
        catch {
            setWarningCreate(true)
        }
    }

    const DeleteProvince = async () => {
        await axios.delete("http://localhost:8080/api/v1/province/delete/" + idProvince, config)
        const response = await axios('http://localhost:8080/api/v1/province/?page=' + page, config);
        setProvinces(response.data.provinces);
    }

    const EditProvince = async () => {
        const province = {
            "code": idProvince,
            "name": nameProvince,
            "administrativeUnit": {
                "id": unitProvince,
                "fullName": administrativeUnitName,
                "shortName": administrativeUnitShortName
            },
            "administrativeRegion": {
                "id": regionProvince,
                "name": administrativeRegionName
            },
            "administrativeCode": administrativeCode
        }

        if (String(unitProvince) === '1') {
            province.administrativeUnit.fullName = "Thành phố trực thuộc trung ương";
            province.administrativeUnit.shortName = "Thành phố";
        } else if (String(unitProvince) === '2') {
            province.administrativeUnit.fullName = "Tỉnh";
            province.administrativeUnit.shortName = "Tỉnh";
        }
        for (let i = 0; i < regions.length; i++) {
            if (regionProvince === String(regions[i].id)) {
                province.administrativeRegion.name = regions[i].name
            }
        }

        try {
            await axios.put("http://localhost:8080/api/v1/province/save/" + defalutIdProvince, province, config)
            setShowWarning(false)
            setShowEdit(false)
            const response = await axios('http://localhost:8080/api/v1/province/?page=' + page, config);
            setProvinces(response.data.provinces);
        } catch {
            setShowWarning(true)
        }
    }

    const CheckedIdNewProvince = async () => {
        setCheckedId(0)
        try {
            const response = await axios.get("http://localhost:8080/api/v1/province/" + idProvince, config)
            setCheckedId(2)
        } catch {
            setCheckedId(1)
        }
    }


    const ModalProvince = () => {
        return (
            <Modal show={show}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Khai báo tỉnh/thành phố</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên tỉnh/thành phố (*)</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameProvince}
                                onChange={(e) => {
                                    setNameProvince(e.target.value)
                                }}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Cấp mã cho tỉnh/thành phố (**)</Form.Label>
                            <Form.Control
                                type="text"
                                value={idProvince}
                                onChange={(e) => {
                                    setIdProvince(e.target.value)
                                    setCheckedId(0)
                                    if(e.target.value.length === 0) setCheckedId(-1)
                                }}
                            />
                            {(checkedId === 0) ? <div className='checked' onClick={() => CheckedIdNewProvince()}><BiCheckCircle className="iconChecked" />Kiểm tra trùng lặp</div> : null}
                            {(checkedId === 2) ? <div className="warningChecked">Mã đơn vị hành chính bạn vừa nhập đang bị trùng với một đơn vị hành chính có sẵn</div> : null}
                            {(checkedId === 1) ? <div className="successChecked">Bạn có thể sử dụng mã vừa nhập</div> : null}
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select value={unitProvince}
                                onChange={(e) => { setUnitProvince(e.target.value) }}><option></option><option value={1} >1. Thành phố trực thuộc trung ương</option><option value={2}>2. Tỉnh</option></Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Khu vực</Form.Label>
                            <Form.Select value={regionProvince}
                                onChange={(e) => { setRegionProvince(e.target.value) }}><option></option>{listRegionItems}</Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã hành chính</Form.Label>
                            <Form.Control type="text" value={administrativeCode}
                                onChange={(e) => { setAdministrativeCode(e.target.value) }} />
                        </Form.Group>
                    </Form>
                    {(showWarningCreate) ? <div className="noteWarning"><p>THÊM TỈNH THÀNH PHỐ MỚI KHÔNG THÀNH CÔNG</p></div> : null}
                </Modal.Body>
                <Modal.Footer>
                    <div className="note">
                        <p>(*) Tên tỉnh/thành phố không được trùng lặp với tên tỉnh/thành phố đã được khai báo</p>
                        <p>(**) Mã số của tỉnh/thành phố không được trùng lặp với mã số của tỉnh/thành phố đã được khai báo</p>
                    </div>
                    <Button variant="secondary" onClick={() => {
                        setShow(false)
                        setWarningCreate(false)
                        setCheckedId(-1)
                    }}>
                        Đóng
                    </Button>
                    <Button variant="primary" onClick={() => { CreateNewProvince() }}>
                        Lưu
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const Pagination = () => {
        return (
            <div className="panigation">
                {(page !== 1) ? <div className="page" onClick={() => {
                    setPage(1)
                    fetchFullProvince(1)
                }}><BsChevronDoubleLeft /></div> : null}
                {(page !== 1) ? <div className="page" onClick={() => {
                    setPage(page - 1)
                    fetchFullProvince(page - 1)
                }}><BsChevronLeft /></div> : null}
                <div className="pageNumber">{page}</div>
                {(page !== numberPage) ? <div className="page" onClick={() => {
                    setPage(page + 1)
                    fetchFullProvince(page + 1)
                }}><BsChevronRight /></div> : null}
                {(page !== numberPage) ? <div className="page" onClick={() => {
                    setPage(numberPage)
                    fetchFullProvince(numberPage)
                }}><BsChevronDoubleRight /></div> : null}
            </div>
        )
    }

    const ModalEditProvince = () => {
        return (
            <Modal show={showEdit}>
                <Modal.Header className='headerModal'>
                    <Modal.Title className='titleModal'>Cập nhật tỉnh/thành phố</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3">
                            <Form.Label>Tên tỉnh/thành phố</Form.Label>
                            <Form.Control
                                type="text"
                                value={nameProvince}
                                onChange={(e) => {
                                    setNameProvince(e.target.value)
                                }}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã tỉnh/thành phố</Form.Label>
                            <Form.Control
                                type="number"
                                value={idProvince}
                                onChange={(e) => {
                                    setIdProvince(e.target.value)
                                }}
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Đơn vị</Form.Label>
                            <Form.Select value={unitProvince} onChange={(e) => {
                                setUnitProvince(e.target.value)
                            }}><option></option><option value='1'>1. Thành phố trực thuộc trung ương</option><option value='2'>2. Tỉnh</option></Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Khu vực</Form.Label>
                            <Form.Select value={regionProvince} onChange={(e) => {
                                setRegionProvince(e.target.value)
                            }}>{listRegionItems}</Form.Select>
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                        >
                            <Form.Label>Mã hành chính</Form.Label>
                            <Form.Control type="text" value={administrativeCode}
                                onChange={(e) => { setAdministrativeCode(e.target.value) }} />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                {(showWarning) ? <div className="noteWarning"><p>THAY ĐỔI THÔNG TIN KHÔNG THÀNH CÔNG</p></div> : null}
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        setShowWarning(false)
                        provinces.map((item) => {
                            if (item.code === idProvince) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Đóng
                    </Button>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        EditProvince()
                        provinces.map((item) => {
                            if (item.code === idProvince) {
                                item.isActive = false;
                            }
                        })
                    }}>
                        Lưu
                    </Button>
                    <Button variant="secondary" onClick={() => {
                        setShowEdit(false)
                        DeleteProvince()
                    }}>
                        Xóa
                    </Button>
                </Modal.Footer>
            </Modal>
        )
    }

    const listProvinces = provinces.map((post) =>
        <tr key={post.code} value={post.code} onClick={() => {
            fetchDetailProvince(post.code)
            post.isActive = true
        }} className="rowTable" style={{ backgroundColor: (post.isActive) ? "yellow" : "white" }}>
            <td>{post.code}</td>
            <td>{post.name}</td>
            <td>{post.administrativeUnit.fullName}</td>
            <td>{post.administrativeRegion.name}</td>
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
                                <th>Đơn vị</th>
                                <th>Tỉnh/Thành phố</th>
                                <th>Khu vực</th>
                            </tr>
                        </thead>
                        <tbody>
                            {listProvinces}
                        </tbody>
                    </Table>
                </div>
            </div>
        )
    }


    return (
        <div>
            <NavbarPage />
            <Button className="buttonAdd" onClick={() => { CreateProvince() }}>+ Khai báo tỉnh/thành phố</Button>
            <div>
                <TableResidential />
                {(show) ? ModalProvince() : null}
                {ModalEditProvince()}
                <Pagination />
            </div>
        </div>
    );
}

export default Province;
