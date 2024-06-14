import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {api} from "@/api/api.ts";
import {useEffect, useState} from "react";
import {arrayToDate, ParkingEventType, ReservationDetailsType} from "@/types/Reservations.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink, PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {Pathnames} from "@/router/pathnames.ts";

function ReservationDetailsPage() {
    const [currentPage, setCurrentPage] = useState(() => parseInt("0"));
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const pageSize = 5;
    const [reservation, setReservation] = useState<ReservationDetailsType | null>(null);
    const navigate = useNavigate();

    const fetchParkingSectors = (page?: number) => {
        if (id) {
            const actualPage = page != undefined ? page : currentPage;
            const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;
            api.getReservationDetails(id, details)
                .then(response => {
                    const updatedReservation = response.data
                    updatedReservation.beginTime = arrayToDate(updatedReservation.beginTime);
                    updatedReservation.endingTime = updatedReservation.endingTime ? arrayToDate(updatedReservation.endingTime) : null;
                    updatedReservation.parkingEvents = response.data.parkingEvents.map((parkingEvent: ParkingEventType) => {
                        return {
                            ...parkingEvent,
                            // @ts-expect-error ignore this for now
                            date: arrayToDate(parkingEvent.date),
                        };
                    });
                    setReservation(updatedReservation);
                    setCurrentPage(actualPage);
                })
                .catch(error => {
                    handleApiError(error);
                });
        }
    };

    useEffect(() => {
        fetchParkingSectors();
    }, []);

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.staff.allReservations)}>All reservations</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>Reservation details</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">Reservation Info</h1>
            </div>
            <div>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{"Begin Time"}</TableHead>
                            <TableHead
                                className="text-center">{"Ending Time"}</TableHead>
                            <TableHead
                                className="text-center">{"City"}</TableHead>
                            <TableHead
                                className="text-center">{"Street"}</TableHead>
                            <TableHead
                                className="text-center">{"Zip Code"}</TableHead>
                            <TableHead
                                className="text-center">{"Sector Name"}</TableHead>
                            <TableHead
                                className="text-center">{"Status"}</TableHead>
                            <TableHead
                                className="text-center">{"ID"}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        <TableRow key={reservation?.id} className="flex-auto">
                            <TableCell>{reservation?.beginTime}</TableCell>
                            <TableCell>{reservation?.endingTime}</TableCell>
                            <TableCell>{reservation?.city}</TableCell>
                            <TableCell>{reservation?.street}</TableCell>
                            <TableCell>{reservation?.zipCode}</TableCell>
                            <TableCell>{reservation?.sectorName}</TableCell>
                            <TableCell>{reservation?.status}</TableCell>
                            <TableCell>{reservation?.id}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">Parking Events</h1>
            </div>
            <Table className="p-10 flex-grow">
                <TableHeader>
                    <TableRow className={"text-center p-10"}>
                        <TableHead
                            className="text-center">{"No."}</TableHead>
                        <TableHead
                            className="text-center">{"Date"}</TableHead>
                        <TableHead
                            className="text-center">{"Type"}</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody className={"text-center"}>
                    {//@ts-expect-error habibi
                        reservation?.parkingEvents.map((parkingEvent, index) => (
                        <TableRow key={parkingEvent.id} className="flex-auto">
                            <TableCell>{index + 1}</TableCell>
                            <TableCell>{parkingEvent.date}</TableCell>
                            <TableCell>{parkingEvent.type}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
            <div className={"pt-5"}>
                <Pagination>
                    <PaginationContent>
                        <PaginationItem>
                            <PaginationPrevious
                                onClick={() => {
                                    if (currentPage > 0) setCurrentPage(currentPage - 1)
                                }}
                            />
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink>{currentPage + 1}</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationNext
                                onClick={() => {
                                    //@ts-expect-error come to dubai
                                    if (reservation?.parkingEvents.length === pageSize) setCurrentPage(currentPage + 1)
                                }}
                            />
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
            </div>
        </div>
    );

}

export default ReservationDetailsPage;