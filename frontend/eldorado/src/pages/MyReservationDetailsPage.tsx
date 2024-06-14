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
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {Pathnames} from "@/router/pathnames.ts";

function MyReservationDetailsPage() {
    const [currentPage, setCurrentPage] = useState(() => parseInt("0"));
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const pageSize = 5;
    const [reservation, setReservation] = useState<ReservationDetailsType | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const navigate = useNavigate();

    const fetchParkingSectors = (page?: number) => {
        if (id) {
            const actualPage = page !== undefined ? page : currentPage;
            const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;
            api.getMyReservationDetails(id, details)
                .then(response => {
                    const updatedReservation = response.data;
                    updatedReservation.beginTime = arrayToDate(updatedReservation.beginTime);
                    updatedReservation.endingTime = updatedReservation.endingTime ? arrayToDate(updatedReservation.endingTime) : null;

                    const parkingEvents = response.data.parkingEvents || [];
                    updatedReservation.parkingEvents = parkingEvents.map((parkingEvent: ParkingEventType) => {
                        return {
                            ...parkingEvent,
                            // @ts-expect-error what?
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

    const handleCancelReservation = async () => {
        if (id) {
            api.cancelReservation(id).then(() => {
                navigate("/my-reservations")
            }).catch(error => {
                handleApiError(error)
            });
        }
    };


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
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.client.myReservations)}>{t("breadcrumb.my.reservations")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.my.reservation.details")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">{t("my.reservation.details.page.reservation.info")}</h1>
            </div>
            <div>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead className="text-center">{"Begin Time"}</TableHead>
                            <TableHead className="text-center">{"Ending Time"}</TableHead>
                            <TableHead className="text-center">{"City"}</TableHead>
                            <TableHead className="text-center">{"Street"}</TableHead>
                            <TableHead className="text-center">{"Zip Code"}</TableHead>
                            <TableHead className="text-center">{"Sector Name"}</TableHead>
                            <TableHead className="text-center">{"ID"}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        <TableRow key={reservation?.id} className="flex-auto">
                            <TableCell>{reservation?.beginTime.toLocaleString()}</TableCell>
                            <TableCell>{reservation?.endingTime?.toLocaleString()}</TableCell>
                            <TableCell>{reservation?.city}</TableCell>
                            <TableCell>{reservation?.street}</TableCell>
                            <TableCell>{reservation?.zipCode}</TableCell>
                            <TableCell>{reservation?.sectorName}</TableCell>
                            <TableCell>{reservation?.id}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">{t("my.reservation.details.page.parking.events")}</h1>
            </div>
            <Table className="p-10 flex-grow">
                <TableHeader>
                    <TableRow className={"text-center p-10"}>
                        <TableHead className="text-center">{t("my.reservation.details.page.parking.no")}</TableHead>
                        <TableHead className="text-center">{t("my.reservation.details.page.parking.date")}</TableHead>
                        <TableHead className="text-center">{t("my.reservation.details.page.parking.type")}</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody className={"text-center"}>
                    {reservation?.parkingEvents?.map((parkingEvent, index) => (
                        <TableRow key={parkingEvent.id} className="flex-auto">
                            <TableCell>{index + 1}</TableCell>
                            <TableCell>{parkingEvent.date.toLocaleString()}</TableCell>
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
                                    if (reservation?.parkingEvents?.length === pageSize) setCurrentPage(currentPage + 1)
                                }}
                            />
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
                <div className="pt-5 flex justify-center">
                    <Button variant="destructive" onClick={() => setIsDialogOpen(true)}>
                        {t("my.reservation.details.page.cancel.reservation")}
                    </Button>
                </div>
                <AlertDialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                    <AlertDialogContent>
                        <AlertDialogHeader>
                            <AlertDialogTitle>{t("my.reservation.details.page.cancel.reservation")}</AlertDialogTitle>
                            <AlertDialogDescription>
                                {t("my.reservation.details.page.cancel.are.you.sure.you.want.to.cancel")}
                            </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                            <AlertDialogCancel onClick={() => setIsDialogOpen(false)}>No</AlertDialogCancel>
                            <AlertDialogAction onClick={handleCancelReservation}>Yes</AlertDialogAction>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialog>
            </div>
        </div>
    );
}

export default MyReservationDetailsPage;
