import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction, useState} from "react";
import {Loader2} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {CreateSectorType, SectorType} from "@/types/Parking.ts";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";

type createParkingFormProps = {
    setDialogOpen: Dispatch<SetStateAction<boolean>>
    refresh: () => void
    parkingId: string
}

function CreateParkingForm({setDialogOpen, refresh, parkingId}:createParkingFormProps) {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [newSector, setNewSector] = useState<CreateSectorType>({name:"", type:SectorType.UNCOVERED, maxPlaces:0, weight:1 })
    const formSchema = z.object({
        name: z.string().min(5, {message: "ZMIEN"})
            .max(5, {message: "ZMIEN"}).regex(RegExp("^[A-Z]{2}-[0-9]{2}$"), {message: "ZMIEN"}),
        maxPlaces: z.coerce.number({message: "ZMIEN"}).int({message: "ZMIEN"}).min(0, {message: "ZMIEN"}).max(1000,{message: "ZMIEN"}),
        type: z.enum(["UNCOVERED", "COVERED", "UNDERGROUND"]),
        weight: z.coerce.number({message: "ZMIEN"}).int({message: "ZMIEN"}).min(1, {message: "ZMIEN"}).max(100,{message: "ZMIEN"})
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: "",
            maxPlaces: 0,
            type: "UNCOVERED",
            weight: 1
        },
    })

    async function handleAlertDialog(){
        api.createSector(parkingId, newSector)
            .then(() => {
                refresh();
                setAlertDialogOpen(false);
                setDialogOpen(false)})
            .catch(error => {
                setAlertDialogOpen(false);
                handleApiError(error);
            });
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        try {
            setNewSector({
                name: values.name,
                type: SectorType[values.type],
                maxPlaces: values.maxPlaces,
                weight: values.weight
            })
            setAlertDialogOpen(true);
        } catch (error) {
            console.log(error);
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                    control={form.control}
                    name="name"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">Name</FormLabel>
                                <FormControl>
                                    <Input placeholder="AA-01" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                control={form.control}
                name="maxPlaces"
                render={({field}) => (
                    <FormItem>
                        <div className="grid gap-4">
                            <FormLabel className="text-left">Max places</FormLabel>
                            <FormControl>
                                <Input placeholder="0" {...field} />
                            </FormControl>
                            <FormMessage/>
                        </div>
                    </FormItem>
                )}
                />
                <FormField
                    control={form.control}
                    name="weight"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">Weight</FormLabel>
                                <FormControl>
                                    <Input placeholder="0" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="type"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">Sector Type</FormLabel>
                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                    <FormControl>
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select a sector type." />
                                        </SelectTrigger>
                                    </FormControl>
                                    <SelectContent>
                                        <SelectItem value="UNCOVERED">Uncovered</SelectItem>
                                        <SelectItem value="COVERED">Covered</SelectItem>
                                        <SelectItem value="UNDERGROUND">Underground</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        </FormItem>
                    )}
                />
                <Button type="submit" className="w-full" disabled={isLoading}>
                    {isLoading ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("parkingManagementPage.createParking")
                    )}
                </Button>
            </form>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        Are you sure you want to create a new sector?
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={handleAlertDialog}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    )
}

export default CreateParkingForm
