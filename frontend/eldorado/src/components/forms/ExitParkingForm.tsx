import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {useState} from "react";
import {Loader2} from "lucide-react";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {Checkbox} from "@/components/ui/checkbox.tsx";

type ExitParkingFormProps = {
    isAuthenticated: boolean
}

function ExitParkingForm({isAuthenticated}:ExitParkingFormProps) {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [exitValues, setExitValues] = useState<{id:string, endReservation:boolean}>({id:"",endReservation:true});
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const formSchema = z.object({
        id: z.string().min(36, {message: "ZMIEN"})
            .max(36, {message: "ZMIEN"}).regex(RegExp("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"), {message: "ZMIEN"}),
        endReservation: z.boolean({message:"ZMIEN"})
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            id: "",
            endReservation: !isAuthenticated
        },
    })

    async function handleAlertDialog(){
        api.exitParking(exitValues.id, exitValues.endReservation, isAuthenticated)
            .then(() => {
                setAlertDialogOpen(false);
            }).catch(error => {
                setAlertDialogOpen(false);
                handleApiError(error);
            });
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        try {
            setExitValues(values);
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
                    name="id"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">ID of the reservation</FormLabel>
                                <FormControl>
                                    <Input placeholder="25268941-b7ad-4288-b4bd-e0a3946b0bd8" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                {isAuthenticated && <FormField
                    control={form.control}
                    name="endReservation"
                    render={({field}) => (
                        <FormItem className="flex flex-row items-start space-x-3 space-y-0 rounded-md border p-4">
                            <FormControl>
                                <Checkbox
                                    checked={field.value}
                                    onCheckedChange={field.onChange}
                                />
                            </FormControl>
                            <div className="space-y-1 leading-none">
                                <FormLabel>
                                   End Reservation
                                </FormLabel>
                            </div>
                        </FormItem>
                    )}
                />}
                <Button type="submit" className="w-full" disabled={isLoading}>
                    {isLoading ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("Exit")
                    )}
                </Button>
            </form>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        Are you sure you want to exit the parking?
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

export default ExitParkingForm
